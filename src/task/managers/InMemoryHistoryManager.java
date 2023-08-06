package task.managers;

import task.taskData.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }
    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (historyList.size() >= 10) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

}
