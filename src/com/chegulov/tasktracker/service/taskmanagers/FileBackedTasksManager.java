package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.model.*;
import com.chegulov.tasktracker.service.exceptions.ManagerSaveException;
import com.chegulov.tasktracker.service.historymanagers.HistoryManager;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;
    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            while(reader.ready()) {
                String line = reader.readLine();
                if (line.isBlank()) break;
                Task task = fileBackedTasksManager.fromString(line);
                if (task != null) {
                    if (task instanceof Epic) {
                        fileBackedTasksManager.epicTasks.put(task.getId(), (Epic) task);
                    } else if (task instanceof SubTask) {
                        fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                        fileBackedTasksManager.prioritizedTasks.add(task);
                    } else {
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                        fileBackedTasksManager.prioritizedTasks.add(task);
                    }
                    fileBackedTasksManager.id = Math.max(fileBackedTasksManager.id, task.getId());
                }
                for (SubTask subTask : fileBackedTasksManager.subTasks.values()) {
                    fileBackedTasksManager.epicTasks.get(subTask.getParentTaskId()).addSubTask(subTask.getId(), subTask);
                }
            }
            if (reader.ready()) {
                String historyLine = reader.readLine();
                List<Integer> history = historyFromString(historyLine);
                for (int id : history) {
                    if (fileBackedTasksManager.tasks.containsKey(id)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.tasks.get(id));
                    } else if (fileBackedTasksManager.subTasks.containsKey(id)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subTasks.get(id));
                    } else if (fileBackedTasksManager.epicTasks.containsKey(id)) {
                        fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicTasks.get(id));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("loadFromFile failed: " + e.getMessage());
        }
        return fileBackedTasksManager;
    }

    private static String historyToString(HistoryManager manager) {
        List<String> ids = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            ids.add(String.valueOf(task.getId()));
        }
        return String.join(",", ids);
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] split = value.split(",");
        for (String num : split) {
            history.add(Integer.parseInt(num));
        }
        return history;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : tasks.values()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : epicTasks.values()) {
                writer.write(epic.toString() + "\n");
            }
            for (SubTask subTask : subTasks.values()) {
                writer.write(subTask.toString() + "\n");
            }
            writer.newLine();
            writer.write(FileBackedTasksManager.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("save failed: " + e.getMessage());
        }
    }

    private Task fromString(String value) {
        String[] params = value.split(",");
        LocalDateTime startTime;
        if (params[6].equals("null")) {
            startTime = null;
        } else {
            startTime = LocalDateTime.parse(params[6]);
        }
        switch (TaskType.valueOf(params[1])) {
            case TASK:
                Task task = new Task(params[2], params[4], Status.valueOf(params[3]), Long.parseLong(params[5]), startTime);
                task.setId(Integer.parseInt(params[0]));
                return task;
            case EPIC:
                Epic epic = new Epic(params[2],params[4]);
                epic.setId(Integer.parseInt(params[0]));
                return epic;
            case SUBTASK:
                SubTask subTask;
                subTask = new SubTask(params[2], params[4], Status.valueOf(params[3]), Long.parseLong(params[5]), startTime, Integer.parseInt(params[7]));
                subTask.setId(Integer.parseInt(params[0]));
                return subTask;
            default:
                return null;
        }
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
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
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicTaskById(int id) {
        Epic epic = super.getEpicTaskById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
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
}
