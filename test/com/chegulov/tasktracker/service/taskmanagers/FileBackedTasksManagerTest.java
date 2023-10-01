package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.service.exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    public void beforeEach() {
        file = new File("test/com/chegulov/tasktracker/resources/test.csv");
        init();
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    void shouldLoadFromFileIfEmptyFile() throws IOException {
        File emptyFile = new File("test/com/chegulov/tasktracker/resources/emptyTest.csv");
        if (!emptyFile.exists()) {
            emptyFile.createNewFile();
        }

        taskManager = FileBackedTasksManager.loadFromFile(emptyFile);

        assertNotNull(taskManager);
    }

    @Test
    void shouldThrowExceptionIfFileNotExist() {
        final ManagerSaveException exception = assertThrows(
                ManagerSaveException.class,
                () -> {
                    File noFile = new File("");
                    taskManager = FileBackedTasksManager.loadFromFile(noFile);
                }
                );

        assertNotNull( exception.getMessage());
        assertEquals(exception.getMessage(), "loadFromFile failed: (Системе не удается найти указанный путь)");
    }

    @Test
    void shouldLoadIfHistoryIsEmpty() {
        taskManager.addTask(task);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subTask1);

        FileBackedTasksManager tasksManager2 = FileBackedTasksManager.loadFromFile(file);
        assertNotNull(tasksManager2);
    }

    @Test
    void shouldLoadFromFile() {
        taskManager.addTask(task);
        taskManager.addEpicTask(epic);
        subTask1.setParentTaskId(epic.getId());
        taskManager.addSubTask(subTask1);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);

        FileBackedTasksManager taskManager2 = FileBackedTasksManager.loadFromFile(file);

        assertNotNull(taskManager2);
        assertEquals(taskManager.getTasks(),taskManager2.getTasks());
        assertEquals(taskManager.getEpicTasks(), taskManager2.getEpicTasks());
        assertEquals(taskManager.getSubTasks(),taskManager2.getSubTasks());
        assertEquals(taskManager.getHistory(), taskManager2.getHistory());
    }

}