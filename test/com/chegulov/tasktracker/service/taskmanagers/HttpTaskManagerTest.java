package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.server.HttpTaskServer;
import com.chegulov.tasktracker.server.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager>{
    private static final String url = "http://localhost:8078/";
    private KVServer kvServer;

    @BeforeEach
    public void beforeEach() throws IOException {
        init();
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(url);
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    void shouldLoadFromServer() {
        taskManager.addEpicTask(epic);
        taskManager.addTask(task);
        taskManager.addSubTask(subTask1);
        taskManager.getTaskById(2);
        taskManager.getEpicTaskById(1);
        taskManager.getSubTaskById(3);

        TaskManager taskManager2 = new HttpTaskManager(url);

        assertEquals(taskManager.getEpicTasks(), taskManager2.getEpicTasks());
        assertEquals(taskManager.getTasks(), taskManager2.getTasks());
        assertEquals(taskManager.getSubTasks(), taskManager2.getSubTasks());
        assertEquals(taskManager.getHistory(), taskManager2.getHistory());
    }
}
