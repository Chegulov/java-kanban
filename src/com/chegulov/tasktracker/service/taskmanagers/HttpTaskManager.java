package com.chegulov.tasktracker.service.taskmanagers;

import com.chegulov.tasktracker.adapters.LocalDateTimeAdapter;
import com.chegulov.tasktracker.client.KVTaskClient;
import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final String url;
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager(String url) {
        super(url);
        this.url = url;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        kvTaskClient = new KVTaskClient(url);
        load();
    }

    @Override
    protected void save() {
        String saveTasks = gson.toJson(tasks.values());
        kvTaskClient.put("task", saveTasks);
        String saveEpics = gson.toJson(epicTasks.values());
        kvTaskClient.put("epic", saveEpics);
        System.out.println(epicTasks.values());
        System.out.println(saveEpics);
        String saveSubTasks = gson.toJson(subTasks.values());
        kvTaskClient.put("subtask", saveSubTasks);
        String saveHistory = gson.toJson(historyToString(historyManager));
        kvTaskClient.put("history", saveHistory);

    }

    private void load() {
        String loadedTasks = kvTaskClient.load("task");
        if (loadedTasks != null) {
            List<Task> loadTasksList = gson.fromJson(loadedTasks, new TypeToken<>() {});
            if (loadTasksList != null && !loadTasksList.isEmpty()) {
                loadTasksList.forEach(task -> {
                            tasks.put(task.getId(), task);
                            prioritizedTasks.add(task);
                        }
                );
            }
        }



        String loadedEpics = kvTaskClient.load("epic");
        if (loadedEpics != null) {
            List<Epic> loadEpicsList = gson.fromJson(loadedEpics, new TypeToken<>() {});
            if (loadEpicsList != null && !loadEpicsList.isEmpty()) {
                loadEpicsList.forEach(epic -> {
                            epicTasks.put(epic.getId(), epic);
                            prioritizedTasks.add(epic);
                        }
                );
            }

        }

        String loadedSubTasks = kvTaskClient.load("subtask");
        if (loadedSubTasks != null) {
            List<SubTask> loadSubTasksList = gson.fromJson(loadedSubTasks, new TypeToken<>() {});
            if (loadSubTasksList != null && !loadSubTasksList.isEmpty()) {
                loadSubTasksList.forEach(subTask -> {
                            subTasks.put(subTask.getId(), subTask);
                            prioritizedTasks.add(subTask);
                        }
                );
            }
        }

        String loadedHistory = kvTaskClient.load("history");
        if (loadedHistory != null && !loadedHistory.isBlank()) {
            loadedHistory = loadedHistory.substring(1,loadedHistory.length()-1);
            historyFromString(loadedHistory);
        }

        for (SubTask subTask : subTasks.values()) {
            epicTasks.get(subTask.getParentTaskId()).addSubTask(subTask.getId(), subTask);
        }
    }
}
