package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.chegulov.tasktracker.service.exceptions.ManagerSaveException;

import java.io.*;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    String filename;
    public FileBackedTasksManager(String filename) {
        super();
        this.filename = filename;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(filename,true)){
            BufferedWriter writer = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public void setTasks(Map<Integer, Task> tasks) {
        super.setTasks(tasks);
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        super.setSubTasks(subTasks);
    }

    @Override
    public Map<Integer, Epic> getEpicTasks() {
        return super.getEpicTasks();
    }

    @Override
    public void setEpicTasks(Map<Integer, Epic> epicTasks) {
        super.setEpicTasks(epicTasks);
    }

    @Override
    public void newEpicTask(Epic epic) {
        super.newEpicTask(epic);
        save();
    }

    @Override
    public void newSubTask(SubTask subTask) {
        super.newSubTask(subTask);
        save();
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicTaskById(int id) {
        return super.getEpicTaskById(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return super.getSubTaskById(id);
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateSubTask(int id, SubTask subTask) {
        super.updateSubTask(id, subTask);
        save();
    }

    @Override
    public void updateEpicTask(int id, Epic epic) {
        super.updateEpicTask(id, epic);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    @Override
    public void removeEpicTask(int id) {
        super.removeEpicTask(id);
        save();
    }

    @Override
    public Map<Integer, SubTask> getSubTaskMapByEpic(int id) {
        return super.getSubTaskMapByEpic(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
