package com.chegulov.tasktracker.service.taskmanagers;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void BeforeEach() {
        init();
        taskManager = new InMemoryTaskManager();
    }
}