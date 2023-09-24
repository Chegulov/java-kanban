package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getTasks();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Epic> getEpicTasks();

    void addEpicTask(Epic epic);

    void addSubTask(SubTask subTask);

    void addTask(Task task);

    void clearTasks();

    void clearEpicTasks();

    void clearSubTasks();

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
