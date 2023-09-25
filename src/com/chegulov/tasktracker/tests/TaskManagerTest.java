package com.chegulov.tasktracker.tests;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.Status;
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
    Task task;
    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epicTest","sb1, sb2");
        subTask1 = new SubTask("Sb1", "test1", 1);
        subTask2 = new SubTask("Sb2", "test2", Status.DONE, 1);
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
    void shouldCountEpicStatusWhenAddSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        assertEquals(taskManager.getEpicTaskById(1).getStatus(), Status.IN_PROGRESS);
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
    void shouldGetTaskById() {
        taskManager.addTask(task);
        assertNotNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldNotGetTaskByIdIfEmpty() {
        assertNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldNotGetTaskByIdIfWrongId() {
        taskManager.addTask(task);
        assertNull(taskManager.getTaskById(2));
    }

    @Test
    void shouldGetEpicTaskById() {
        taskManager.addEpicTask(epic);
        assertNotNull(taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldNotGetEpicTaskByIdIfEmpty() {
        assertNull(taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldNotGetEpicTaskByIdIfWrongId() {
        taskManager.addEpicTask(epic);
        assertNull(taskManager.getEpicTaskById(2));
    }

    @Test
    void shouldGetSubTaskById() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertNotNull(taskManager.getSubTaskById(2));
    }

    @Test
    void shouldNotGetSubTaskByIdIfEmpty() {
        assertNull(taskManager.getSubTaskById(2));
    }

    @Test
    void shouldNotGetSubTaskByIdIfWrongId() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertNull(taskManager.getSubTaskById(3));
    }

    @Test
    void shouldUpdateTask() {
        taskManager.addTask(task);
        task = new Task("task2", "task2do");
        taskManager.updateTask(1, task);
        assertEquals(taskManager.getTaskById(1).getName(), "task2");
    }

    @Test
    void shouldNotUpdateTaskIfEmpty() {
        taskManager.updateTask(1, task);
        assertNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldNotUpdateTaskIfWrongId() {
        taskManager.addTask(task);
        task = new Task("task2", "task2do");
        taskManager.updateTask(2, task);
        assertNull(taskManager.getTaskById(2));
    }

    @Test
    void shouldUpdateSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        taskManager.updateSubTask(2, subTask2);
        assertEquals(taskManager.getSubTaskById(2).getName(), subTask2.getName());
    }

    @Test
    void shouldNotUpdateSubTaskIfEmpty() {
        taskManager.addEpicTask(epic);
        taskManager.updateSubTask(2, subTask2);
        assertNull(taskManager.getSubTaskById(2));
    }

    @Test
    void shouldNotUpdateSubTaskIfWrongId() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        taskManager.updateSubTask(3, subTask2);
        assertNull(taskManager.getSubTaskById(3));
    }

    @Test
    void shouldNotUpdateSubTaskIfWrongEpicId() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        subTask2.setParentTaskId(4);
        taskManager.updateSubTask(2, subTask2);
        assertNotEquals(taskManager.getSubTaskById(2).getName(), subTask2.getName());
    }

    @Test
    void shouldCountEpicStatusWhenUpdateSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        taskManager.updateSubTask(2, subTask2);
        assertEquals(taskManager.getEpicTaskById(1).getStatus(), Status.DONE);
    }

    @Test
    void shouldUpdateEpicTask() {
        taskManager.addEpicTask(epic);
        epic = new Epic("epic2", "epic2do");
        taskManager.updateEpicTask(1, epic);
        assertEquals(taskManager.getEpicTaskById(1).getName(), epic.getName());
    }

    @Test
    void shouldNotUpdateEpicTaskIfEmpty() {
        taskManager.updateEpicTask(1, epic);
        assertNull(taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldNotUpdateEpicTaskIfWrongId() {
        taskManager.addEpicTask(epic);
        epic = new Epic("epic2", "epic2do");
        taskManager.updateEpicTask(2, epic);
        assertNull(taskManager.getEpicTaskById(2));
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