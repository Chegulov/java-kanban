import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicTaskMap = new HashMap<>();

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
        epicTaskMap.get(subTask.getParentTaskId()).getSubTaskMap().put(id, subTask);
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
}
