public class SubTask extends Task {
    protected int parentTaskId;

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
}
