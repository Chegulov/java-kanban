package com.chegulov.tasktracker.service.taskmanagers;

import java.time.Duration;
import java.util.*;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.Status;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.chegulov.tasktracker.service.historymanagers.HistoryManager;
import com.chegulov.tasktracker.service.Managers;

public class InMemoryTaskManager implements TaskManager {
    protected int id = 0;
    protected Map<Integer, Task> tasks;
    protected Map<Integer, SubTask> subTasks;
    protected Map<Integer, Epic> epicTasks;
    protected final HistoryManager historyManager;
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epicTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(
                        Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId)
        );
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public boolean addEpicTask(Epic epic) {
        if (epic != null) {
            epicTasks.put(++id, epic);
            epic.setId(id);
            determineEpicStatus(epic);
            return true;
        }
        return false;
    }

    @Override
    public boolean addSubTask(SubTask subTask) {
        if (subTask != null) {
            if (epicTasks.containsKey(subTask.getParentTaskId()) && !hasCrossTime(subTask)) {
                subTasks.put(++id, subTask);
                subTask.setId(id);
                Epic epic = epicTasks.get(subTask.getParentTaskId());
                epic.addSubTask(id);
                determineEpicStatus(epic);
                countEpicTime(epic);
                prioritizedTasks.add(subTask);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean addTask(Task task) {
        if (task != null && !hasCrossTime(task)) {
            tasks.put(++id, task);
            task.setId(id);
            prioritizedTasks.add(task);
            return true;
        }
        return false;
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        }
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        // при удалении эпиков, удалить все связанные сабтаски.
        for (Epic epic : epicTasks.values()) {
            for (Integer key : epic.getSubTasks()) {
                prioritizedTasks.remove(subTasks.get(key));
                subTasks.remove(key);
                historyManager.remove(key);
            }
            historyManager.remove(epic.getId());
            epic.getSubTasks().clear();
        }
        epicTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        // при удалении сабтаски, удалить её из связанного эпика.
        for (SubTask subTask : subTasks.values()) {
            int id = subTask.getParentTaskId();
            epicTasks.get(id).getSubTasks().remove(Integer.valueOf(subTask.getId()));
            determineEpicStatus(epicTasks.get(id));
            countEpicTime(epicTasks.get(id));
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
        }
        subTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else return null;
    }

    @Override
    public Epic getEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        } else return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        } else return null;
    }

    @Override
    public boolean updateTask(int id, Task task) {
        if (task != null) {
            if (tasks.containsKey(id)) {
                prioritizedTasks.remove(tasks.get(id));
                if (!hasCrossTime(task)) {
                    task.setId(id);
                    tasks.put(id, task);
                    prioritizedTasks.add(task);
                    return true;
                } else {
                    prioritizedTasks.add(tasks.get(id));
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean updateSubTask(int id, SubTask subTask) {
        if (subTask != null) {
            if (subTasks.containsKey(id) && epicTasks.containsKey(subTask.getParentTaskId())) {
                prioritizedTasks.remove(subTasks.get(id));
                if (!hasCrossTime(subTask)) {
                    subTask.setId(id);
                    int parentTaskId = subTasks.get(id).getParentTaskId();
                    Epic epic = epicTasks.get(parentTaskId);
                    subTasks.put(id, subTask);
                    determineEpicStatus(epic);
                    countEpicTime(epic);
                    prioritizedTasks.add(subTask);
                    return true;
                } else {
                    prioritizedTasks.add(subTasks.get(id));
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean updateEpicTask(int id, Epic epic) {
        if (epic != null) {
            if (epicTasks.containsKey(id)) {
                List<Integer> oldSubTasks = epicTasks.get(id).getSubTasks();
                epic.setId(id);
                epic.setSubTasks(oldSubTasks);
                determineEpicStatus(epic);
                countEpicTime(epic);
                epicTasks.put(id, epic);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTask(int id) {
        if (subTasks.containsKey(id)) {
            int parentTaskId = subTasks.get(id).getParentTaskId();
            Epic epic = epicTasks.get(parentTaskId);
            epic.removeSubTask(id);
            determineEpicStatus(epic);
            countEpicTime(epic);
            subTasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        for (int subTaskKey : epicTasks.get(id).getSubTasks()) {
            subTasks.remove(subTaskKey);
            historyManager.remove(subTaskKey);
        }
        epicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<SubTask> getSubTasksByEpic(int id) {
        List<SubTask> epicSubTasks = new ArrayList<>();
        for (int i : epicTasks.get(id).getSubTasks()) {
            epicSubTasks.add(subTasks.get(i));
        }
        return epicSubTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public boolean hasCrossTime(Task task) {
        if (task.getStartTime() == null) {
            return false;
        }
        return prioritizedTasks.stream()
                .filter(task1 -> task1.getStartTime() != null)
                .anyMatch(task1 -> (
                        (task.getStartTime().isBefore(task1.getEndTime())
                                && task.getStartTime().isAfter(task1.getStartTime()))
                                || (task.getEndTime().isAfter(task1.getStartTime())
                                && task.getEndTime().isBefore(task1.getEndTime()))
                                || (task.getStartTime().isBefore(task1.getStartTime())
                                && task.getEndTime().isAfter(task1.getEndTime()))
                                || (task.getStartTime().equals(task1.getStartTime()))
                                || (task.getEndTime().equals(task1.getEndTime())))
                );
    }

    private void determineEpicStatus(Epic epic) {
        if (!epic.getSubTasks().isEmpty()) {
            Status status = null;
            for (int id : epic.getSubTasks()) {
                SubTask subTask = subTasks.get(id);
                if (status == null) {
                    status = subTask.getStatus();
                } else {
                    if (status != subTask.getStatus()) {
                        status = Status.IN_PROGRESS;
                    }
                }
            }
            epic.setStatus(status);
            return;
        }
        epic.setStatus(Status.NEW);
    }

    private void countEpicTime(Epic epic) {
        if (!epic.getSubTasks().isEmpty()) {
            for (int id : epic.getSubTasks()) {
                SubTask subTask = subTasks.get(id);
                if (subTask.getEndTime() != null) {
                    if (epic.getEndTime() == null || subTask.getEndTime().isAfter(epic.getEndTime())) {
                        epic.setEndTime(subTask.getEndTime());
                    }
                }
                if (subTask.getStartTime() != null) {
                    if (epic.getStartTime() == null || subTask.getStartTime().isBefore(epic.getStartTime())) {
                        epic.setStartTime(subTask.getStartTime());
                    }
                }
            }
            if (epic.getStartTime() != null && epic.getEndTime() != null) {
                epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()).toMinutes());
            }
        }
    }
}
