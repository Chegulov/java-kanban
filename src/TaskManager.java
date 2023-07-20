import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    HashMap<Integer, Epic> epicTaskMap = new HashMap<>();

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
}
