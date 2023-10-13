package com.chegulov.tasktracker;

import com.chegulov.tasktracker.server.HttpTaskServer;
import com.chegulov.tasktracker.service.Managers;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;

import java.io.IOException;

public class HttpTaskServerStarter {
    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}
