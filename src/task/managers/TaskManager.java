package task.managers;

import task.taskData.Epic;
import task.taskData.SubTask;
import task.taskData.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getTasks();

    void setTasks(Map<Integer, Task> tasks);

    Map<Integer, SubTask> getSubTasks();

    void setSubTasks(Map<Integer, SubTask> subTasks);

    Map<Integer, Epic> getEpicTasks();

    void setEpicTasks(Map<Integer, Epic> epicTasks);

    void newEpicTask(Epic epic);

    void newSubTask(SubTask subTask);

    void newTask(Task task);

    void clearTaskMap();

    void clearEpicTaskMap();

    void clearSubTaskMap();

    Task getTaskById(int id);

    Epic getEpicTaskById(int id);

    SubTask getSubTaskById(int id);

    void updateTask(int id, Task task);

    void updateSubTask(int id, SubTask subTask);

    void updateEpicTask(int id, Epic epic);

    void removeTask(int id);

    void removeSubTask(int id);

    void removeEpicTask(int id);

    Map<Integer, SubTask> getSubTaskMapByEpic(int id);

    List<Task> getHistory();
}
