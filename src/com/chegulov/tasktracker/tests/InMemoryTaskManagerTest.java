package com.chegulov.tasktracker.tests;

import com.chegulov.tasktracker.service.taskmanagers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {
    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        taskManager = new InMemoryTaskManager();
    }
}