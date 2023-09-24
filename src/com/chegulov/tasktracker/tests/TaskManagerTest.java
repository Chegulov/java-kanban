package com.chegulov.tasktracker.tests;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    Epic epic;
    Epic emptyEpic;
    SubTask subTask1;
    SubTask subTask2;
    Task task = new Task("Task1", "task");
    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epicTest","sb1, sb2");
        subTask1 = new SubTask("Sb1", "test1", 1);
        subTask2 = new SubTask("Sb2", "test2", 1);
        task = new Task("Task1", "task");
    }
    @Test
    void shouldGetTasks() {
        taskManager.addTask(task);
        assertFalse(taskManager.getTasks().isEmpty());
    }

    @Test
    void shouldNotGetTasks() {
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void shouldGetSubTasks() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertFalse(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldNotGetSubTasks() {
        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldGetEpicTasks() {
        taskManager.addEpicTask(epic);
        assertFalse(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void shouldNotGetEpicTasks() {
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void shouldAddEpicTask() {
        taskManager.addEpicTask(epic);
        assertNotNull(taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldAddSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertNotNull(taskManager.getSubTaskById(2));
    }

    @Test
    void shouldNotAddSubTaskIfEpicNotExist() {
        taskManager.addSubTask(subTask1);
        assertNull(taskManager.getSubTaskById(1));
    }

    @Test
    void shouldAddTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldClearTasks() {
        taskManager.addTask(task);
        taskManager.clearTasks();
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void shouldClearEpicTasks() {
        taskManager.addEpicTask(epic);
        taskManager.clearEpicTasks();
        assertTrue(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void shouldClearSubTasks() {
        taskManager.addSubTask(subTask1);
        taskManager.clearTasks();
        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getEpicTaskById() {
    }

    @Test
    void getSubTaskById() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateSubTask() {
    }

    @Test
    void updateEpicTask() {
    }

    @Test
    void removeTask() {
    }

    @Test
    void removeSubTask() {
    }

    @Test
    void removeEpicTask() {
    }

    @Test
    void getSubTaskMapByEpic() {
    }

    @Test
    void getHistory() {
    }
}