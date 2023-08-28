package com.chegulov.tasktracker.service.historymanagers;

import com.chegulov.tasktracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);

    List<Task> getHistory();
}
