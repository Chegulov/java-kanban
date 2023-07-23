package task.taskData;

public class SubTask extends Task {
    private int parentTaskId;

    public SubTask(String name, String description, int parentTaskId) {
        super(name, description);
        this.parentTaskId = parentTaskId;
    }

    public SubTask(String name, String description, Status status, int parentTaskId) {
        super(name, description, status);
        this.parentTaskId = parentTaskId;
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(int parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String toString() {
        return "task.taskdata.SubTask{id=" + id + ", name=" + name + ", description.length="
                + description.length() + ", task.taskdata.Status=" + status + ", parentTaskId=" + parentTaskId + "}";
    }
}
