package task.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import task.taskData.*;
public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private Map<Integer, Task> taskMap;
    private Map<Integer, SubTask> subTaskMap;
    private Map<Integer, Epic> epicTaskMap;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        taskMap = new HashMap<>();
        subTaskMap = new HashMap<>();
        epicTaskMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    @Override
    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    @Override
    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    @Override
    public void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }

    @Override
    public Map<Integer, Epic> getEpicTaskMap() {
        return epicTaskMap;
    }

    @Override
    public void setEpicTaskMap(HashMap<Integer, Epic> epicTaskMap) {
        this.epicTaskMap = epicTaskMap;
    }

    @Override
    public void newEpicTask(Epic epic) {
        epicTaskMap.put(++id, epic);
        epic.setId(id);
    }

    @Override
    public void newSubTask(SubTask subTask) {
        subTaskMap.put(++id, subTask);
        subTask.setId(id);
        epicTaskMap.get(subTask.getParentTaskId()).addSubTask(id, subTask);
    }

    @Override
    public void newTask(Task task) {
        taskMap.put(++id, task);
        task.setId(id);
    }

    @Override
    public void clearTaskMap() {
        taskMap.clear();
    }

    @Override
    public void clearEpicTaskMap() {
        // при удалении эпиков, удалить все связанные сабтаски.
        for (Epic epic : epicTaskMap.values()) {
            for (Integer key : epic.getSubTaskMap().keySet()) {
                subTaskMap.remove(key);
            }
            epic.getSubTaskMap().clear();
        }
        epicTaskMap.clear();
    }

    @Override
    public void clearSubTaskMap() {
        // при удалении сабтаски, удалить её из связанного эпика.
        for (SubTask subTask : subTaskMap.values()) {
            int id = subTask.getParentTaskId();
            epicTaskMap.get(id).getSubTaskMap().remove(subTask.getId());
        }
        subTaskMap.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            historyManager.add(taskMap.get(id));
            return taskMap.get(id);
        } else return null;
    }

    @Override
    public Epic getEpicTaskById(int id) {
        if (epicTaskMap.containsKey(id)) {
            historyManager.add(epicTaskMap.get(id));
            return epicTaskMap.get(id);
        } else return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTaskMap.containsKey(id)) {
            historyManager.add(subTaskMap.get(id));
            return subTaskMap.get(id);
        } else return null;
    }

    @Override
    public void updateTask(int id, Task task) {
        if (taskMap.containsKey(id)) {
            task.setId(id);
            taskMap.put(id, task);
        }
    }

    @Override
    public void updateSubTask(int id, SubTask subTask) {
        if (subTaskMap.containsKey(id)) {
            subTask.setId(id);
            int parentTaskId = subTaskMap.get(id).getParentTaskId();
            epicTaskMap.get(parentTaskId).removeSubTask(id);
            subTaskMap.put(id, subTask);
            parentTaskId = subTask.getParentTaskId();
            epicTaskMap.get(parentTaskId).addSubTask(id, subTask);
        }
    }

    @Override
    public void updateEpicTask(int id, Epic epic) {
        if (epicTaskMap.containsKey(id)) {
            HashMap<Integer, SubTask> oldSubTaskMap = epicTaskMap.get(id).getSubTaskMap();
            epic.setId(id);
            epic.setSubTaskMap(oldSubTaskMap);
            epicTaskMap.put(id, epic);
        }
    }

    @Override
    public void removeTask(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeSubTask(int id) {
        if (subTaskMap.containsKey(id)) {
            int parentTaskId = subTaskMap.get(id).getParentTaskId();
            epicTaskMap.get(parentTaskId).removeSubTask(id);
            subTaskMap.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        for (int subTaskKey : epicTaskMap.get(id).getSubTaskMap().keySet()) {
            subTaskMap.remove(subTaskKey);
        }
        epicTaskMap.remove(id);
    }

    @Override
    public HashMap<Integer, SubTask> getSubTaskMapByEpic(int id) {
        if (epicTaskMap.containsKey(id)) {
            return epicTaskMap.get(id).getSubTaskMap();
        }
        return new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
