package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public interface TaskManager {

    Map<Integer, Task> getTasks();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Epic> getEpicTasks();

    boolean addEpicTask(Epic epic);

    boolean addSubTask(SubTask subTask);

    boolean addTask(Task task);

    void clearTasks();

    void clearEpicTasks();

    void clearSubTasks();

    Task getTaskById(int id);

    Epic getEpicTaskById(int id);

    SubTask getSubTaskById(int id);

    boolean updateTask(int id, Task task);

    boolean updateSubTask(int id, SubTask subTask);

    boolean updateEpicTask(int id, Epic epic);

    void removeTask(int id);

    void removeSubTask(int id);

    void removeEpicTask(int id);

    Map<Integer, SubTask> getSubTaskMapByEpic(int id);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();

    boolean hasCrossTime(Task task);
}
