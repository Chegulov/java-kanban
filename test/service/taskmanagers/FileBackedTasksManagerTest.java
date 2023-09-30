package service.taskmanagers;

import com.chegulov.tasktracker.service.exceptions.ManagerSaveException;
import com.chegulov.tasktracker.service.taskmanagers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file;
    @Override
    @BeforeEach
    public void beforeEach() {
        file = new File("test/testresources/test.csv");
        super.beforeEach();
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    void shouldLoadFromFileIfEmptyFile() {
        File emptyFile = new File("test/testresources/emptyTest.csv");
        if (!emptyFile.exists()) {
            try {
                emptyFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
            }
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
    }

    @Test
    void souldLoadIfHistoryIsEmpty() {
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
    }

    @Test
    void shouldGetHistory() {
        taskManager.addTask(task);
        taskManager.addEpicTask(epic);
        subTask1.setParentTaskId(epic.getId());
        taskManager.addSubTask(subTask1);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);
        assertEquals(taskManager.getHistory(), List.of(task,epic,subTask1));
    }

    @Test
    void shouldGetEmptyHistory() {
        assertTrue(taskManager.getHistory().isEmpty());
    }

}