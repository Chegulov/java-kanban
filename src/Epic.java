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
    }

    public void addSubTask (SubTask subTask, int id) {
        subTaskMap.put(id,subTask);
    }
}
