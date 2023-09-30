package service.taskmanagers;

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
    Task task2;
    Task task3;
    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epicTest","sb1 sb2");
        subTask1 = new SubTask("Sb1", "test1", 1);
        subTask2 = new SubTask("Sb2", "test2", Status.DONE, 1);
        task = new Task("Task1", "task");
        task2 = new Task("Task2", "task2");
        task3 = new Task("Task3", "task3");
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
    void shouldAddAndGetEpicTask() {
        taskManager.addEpicTask(epic);
        assertNotNull(taskManager.getEpicTaskById(1));
        assertEquals(taskManager.getEpicTaskById(1), epic);
    }

    @Test
    void shouldAddAndGetSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertNotNull(taskManager.getSubTaskById(2));
        assertEquals(taskManager.getSubTaskById(2), subTask1);
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
    void shouldAddAndGetTask() {
        taskManager.addTask(task);
        assertNotNull(taskManager.getTaskById(1));
        assertEquals(taskManager.getTaskById(1), task);
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
    void shouldNotGetTaskByIdIfEmpty() {
        assertNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldNotGetTaskByIdIfWrongId() {
        taskManager.addTask(task);
        assertNull(taskManager.getTaskById(2));
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
        assertEquals(taskManager.getTaskById(1), task);
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
        assertEquals(taskManager.getSubTaskById(2), subTask2);
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
        assertEquals(taskManager.getEpicTaskById(1), epic);
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
    void shouldRemoveTask() {
        taskManager.addTask(task);
        assertFalse(taskManager.getTasks().isEmpty());
        taskManager.removeTask(1);
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void removeSubTask() {
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);
        assertFalse(taskManager.getSubTasks().isEmpty());
        taskManager.removeSubTask(2);
        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void removeEpicTask() {
        taskManager.addEpicTask(epic);
        assertFalse(taskManager.getEpicTasks().isEmpty());
        taskManager.removeEpicTask(1);
        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void getSubTaskMapByEpic() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void shouldDoubleSave() {
        taskManager.addTask(task);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        assertFalse(taskManager.getHistory().isEmpty());
        assertEquals(taskManager.getHistory().size(), 1);
    }

    @Test
    void shouldRemoveFromHistoryHead() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        assertEquals(taskManager.getHistory().size(), 3);
        taskManager.removeTask(1);
        assertEquals(taskManager.getHistory().size(), 2);
        assertEquals(taskManager.getHistory().get(0), task2);
    }

    @Test
    void shouldRemoveEpicWithSTFromHistory() {
        taskManager.addEpicTask(epic);
        taskManager.addTask(task);
        taskManager.addSubTask(subTask1);
        taskManager.getEpicTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getSubTaskById(3);
        assertEquals(taskManager.getHistory().size(), 3);
        taskManager.removeEpicTask(1);
        assertEquals(taskManager.getHistory().size(), 1);
        assertEquals(taskManager.getHistory().get(0), task);
    }

    @Test
    void shouldRemoveFromHistoryMid() {
        taskManager.addEpicTask(epic);
        taskManager.addTask(task);
        taskManager.addSubTask(subTask1);
        taskManager.getEpicTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getSubTaskById(3);
        assertEquals(taskManager.getHistory().size(), 3);
        taskManager.removeTask(2);
        assertEquals(taskManager.getHistory().size(), 2);
        assertEquals(taskManager.getHistory().get(0), epic);
    }

    @Test
    void shouldRemoveFromHistoryTail() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(3);
        assertEquals(taskManager.getHistory().size(), 3);
        taskManager.removeTask(3);
        assertEquals(taskManager.getHistory().size(), 2);
        assertEquals(taskManager.getHistory().get(taskManager.getHistory().size()-1), task2);
    }
}