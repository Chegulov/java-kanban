package task.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import task.taskData.*;
public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private Map<Integer, Task> tasks;
    private Map<Integer, SubTask> subTasks;
    private Map<Integer, Epic> epicTasks;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epicTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public void setTasks(Map<Integer, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    @Override
    public void setEpicTasks(Map<Integer, Epic> epicTasks) {
        this.epicTasks = epicTasks;
    }

    @Override
    public void newEpicTask(Epic epic) {
        epicTasks.put(++id, epic);
        epic.setId(id);
    }

    @Override
    public void newSubTask(SubTask subTask) {
        subTasks.put(++id, subTask);
        subTask.setId(id);
        epicTasks.get(subTask.getParentTaskId()).addSubTask(id, subTask);
    }

    @Override
    public void newTask(Task task) {
        tasks.put(++id, task);
        task.setId(id);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        // при удалении эпиков, удалить все связанные сабтаски.
        for (Epic epic : epicTasks.values()) {
            for (Integer key : epic.getSubTasks().keySet()) {
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
            epicTasks.get(id).getSubTasks().remove(subTask.getId());
            historyManager.remove(subTask.getId());
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
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            task.setId(id);
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubTask(int id, SubTask subTask) {
        if (subTasks.containsKey(id)) {
            subTask.setId(id);
            int parentTaskId = subTasks.get(id).getParentTaskId();
            epicTasks.get(parentTaskId).removeSubTask(id);
            subTasks.put(id, subTask);
            parentTaskId = subTask.getParentTaskId();
            epicTasks.get(parentTaskId).addSubTask(id, subTask);
        }
    }

    @Override
    public void updateEpicTask(int id, Epic epic) {
        if (epicTasks.containsKey(id)) {
            Map<Integer, SubTask> oldSubTaskMap = epicTasks.get(id).getSubTasks();
            epic.setId(id);
            epic.setSubTasks(oldSubTaskMap);
            epicTasks.put(id, epic);
        }
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
            epicTasks.get(parentTaskId).removeSubTask(id);
            subTasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        for (int subTaskKey : epicTasks.get(id).getSubTasks().keySet()) {
            subTasks.remove(subTaskKey);
            historyManager.remove(subTaskKey);
        }
        epicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Map<Integer, SubTask> getSubTaskMapByEpic(int id) {
        if (epicTasks.containsKey(id)) {
            return epicTasks.get(id).getSubTasks();
        }
        return new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
