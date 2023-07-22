import java.util.HashMap;

public class Epic extends Task {

    protected HashMap<Integer, SubTask> subTaskMap;


    public Epic(String name, String description) {
        super(name, description);
        subTaskMap = new HashMap<>();
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
        status = determineStatus();
    }

    public void addSubTask (int id, SubTask subTask) {
        subTaskMap.put(id,subTask);
        status = determineStatus();
    }

    public void removeSubTask(int id) {
        subTaskMap.remove(id);
        status = determineStatus();
    }

    private Status determineStatus() {
        if (!subTaskMap.isEmpty()) {
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
        return Status.NEW;
    }

    @Override
    public String toString() {
        String result = "Epic{id=" + id + ", name=" + name + ", description.length=" +
                description.length() + ", Status=" + status + ", subTaskMap = " + subTaskMap;
        return result;
    }
}
