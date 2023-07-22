import java.util.HashMap;

public class Epic extends Task {

    protected HashMap<Integer, SubTask> subTaskMap;


    public Epic(String name, String description) {
        super(name, description);
        subTaskMap = new HashMap<>();
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subTaskMap = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
        determineStatus();
    }

    public void addSubTask (int id, SubTask subTask) {
        subTaskMap.put(id,subTask);
        if (status != subTask.status) {
            status = Status.IN_PROGRESS;
        }
    }

    public void removeSubTask(int id) {
        subTaskMap.remove(id);
        determineStatus();
    }

    private Status determineStatus() {
        Status status = null;
        for (SubTask subTask : subTaskMap.values()) {
            if (status == null) {
                status = subTask.status;
            } else {
                if (status != subTask.status) {
                    status = Status.IN_PROGRESS;
                }
            }
        }
        return status;
    }
}
