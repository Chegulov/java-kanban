package com.chegulov.tasktracker.service;

import com.chegulov.tasktracker.service.historymanagers.HistoryManager;
import com.chegulov.tasktracker.service.historymanagers.InMemoryHistoryManager;
import com.chegulov.tasktracker.service.taskmanagers.HttpTaskManager;
import com.chegulov.tasktracker.service.taskmanagers.InMemoryTaskManager;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;

import java.net.URI;

public final class Managers {

    private Managers(){}
    private static final String url = "http://localhost:8078/";

    public static TaskManager getDefault() {
        return new HttpTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
