package com.chegulov.tasktracker.service;

import com.chegulov.tasktracker.service.historymanagers.HistoryManager;
import com.chegulov.tasktracker.service.historymanagers.InMemoryHistoryManager;
import com.chegulov.tasktracker.service.taskmanagers.InMemoryTaskManager;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;

public final class Managers {

    private Managers(){}
    public static TaskManager getDefault() {
        return new InMemoryTaskManager() {
        };
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
