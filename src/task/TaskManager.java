package task;

import task.taskData.Epic;
import task.taskData.SubTask;
import task.taskData.Task;

import java.util.HashMap;

public interface TaskManager {

    HashMap<Integer, Task> getTaskMap();

    void setTaskMap(HashMap<Integer, Task> taskMap);

    HashMap<Integer, SubTask> getSubTaskMap();

    void setSubTaskMap(HashMap<Integer, SubTask> subTaskMap);

    HashMap<Integer, Epic> getEpicTaskMap();

    void setEpicTaskMap(HashMap<Integer, Epic> epicTaskMap);

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

    HashMap<Integer, SubTask> getSubTaskMapByEpic(int id);
}
