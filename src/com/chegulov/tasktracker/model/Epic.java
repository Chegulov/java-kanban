package com.chegulov.tasktracker.model;

import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {

    private Map<Integer, SubTask> subTasks;


    public Epic(String name, String description) {
        super(name, description);
        subTasks = new HashMap<>();
    }

    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Map<Integer, SubTask> subTasks) {
        this.subTasks = subTasks;
        status = determineStatus();
    }

    public void addSubTask (int id, SubTask subTask) {
        subTasks.put(id,subTask);
        status = determineStatus();
    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
        status = determineStatus();
    }

    private Status determineStatus() {
        if (!subTasks.isEmpty()) {
            Status status = null;
            for (SubTask subTask : subTasks.values()) {
                if (status == null) {
                    status = subTask.status;
                } else {
                    if (status != subTask.status) {
                        status = Status.IN_PROGRESS;
                    }
                }
            }
            return status;
        }
        return Status.NEW;
    }

    @Override
    public String toString() {
        return "Epic{id=" + id + ", name=" + name + ", description.length=" +
                description.length() + ", status=" + status + ", subTaskMap = " + subTasks + "}";
    }
}
