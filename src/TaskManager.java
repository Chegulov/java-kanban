import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    private HashMap<Integer, Task> taskMap;
    private HashMap<Integer, SubTask> subTaskMap;
    private HashMap<Integer, Epic> epicTaskMap;

    public TaskManager() {
        taskMap = new HashMap<>();
        subTaskMap = new HashMap<>();
        epicTaskMap = new HashMap<>();
    }

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(HashMap<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }

    public HashMap<Integer, Epic> getEpicTaskMap() {
        return epicTaskMap;
    }

    public void setEpicTaskMap(HashMap<Integer, Epic> epicTaskMap) {
        this.epicTaskMap = epicTaskMap;
    }

    public void newEpicTask(Epic epic) {
        epicTaskMap.put(++id, epic);
        epic.setId(id);
    }

    public void newSubTask(SubTask subTask) {
        subTaskMap.put(++id, subTask);
        subTask.setId(id);
        epicTaskMap.get(subTask.getParentTaskId()).addSubTask(id, subTask);
    }

    public void newTask(Task task) {
        taskMap.put(++id, task);
        task.setId(id);
    }

    public void clearTaskMap() {
        taskMap.clear();
    }

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

    public void clearSubTaskMap() {
        // при удалении сабтаски, удалить её из связанного эпика.
        for (SubTask subTask : subTaskMap.values()) {
            int id = subTask.getParentTaskId();
            epicTaskMap.get(id).subTaskMap.remove(subTask.getId());
        }
        subTaskMap.clear();
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Epic getEpicTaskById(int id) {
        return epicTaskMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTaskMap.get(id);
    }

    public void updateTask(int id, Task task) {
        if (taskMap.containsKey(id)) {
            task.setId(id);
            taskMap.put(id, task);
        }
    }

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

    public void updateEpicTask(int id, Epic epic) {
        if (epicTaskMap.containsKey(id)) {
            HashMap<Integer, SubTask> oldSubTaskMap = epicTaskMap.get(id).getSubTaskMap();
            epic.setId(id);
            epic.setSubTaskMap(oldSubTaskMap);
            epicTaskMap.put(id, epic);
        }
    }

    public void removeTask(int id) {
        taskMap.remove(id);
    }

    public void removeSubTask(int id) {
        if (subTaskMap.containsKey(id)) {
            int parentTaskId = subTaskMap.get(id).getParentTaskId();
            epicTaskMap.get(parentTaskId).removeSubTask(id);
            subTaskMap.remove(id);
        }
    }

    public void removeEpicTask(int id) {
        for (int subTaskKey : epicTaskMap.get(id).getSubTaskMap().keySet()) {
            subTaskMap.remove(subTaskKey);
        }
        epicTaskMap.remove(id);
    }

    public HashMap<Integer, SubTask> getSubTaskMapByEpic (int id) {
        if (epicTaskMap.containsKey(id)) {
            return epicTaskMap.get(id).getSubTaskMap();
        }
        return null;
    }
}
