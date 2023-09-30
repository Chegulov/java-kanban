package com.chegulov.tasktracker.model;

import java.time.LocalDateTime;

public class SubTask extends Task {
    private int parentTaskId;

    public SubTask(String name, String description, int parentTaskId) {
        super(name, description);
        this.parentTaskId = parentTaskId;
    }

    public SubTask(String name, String description, Status status, int parentTaskId) {
        super(name, description, status);
        this.parentTaskId = parentTaskId;
    }

    public SubTask(String name, String description, long duration, LocalDateTime startTime, int parentTaskId) {
        super(name, description, duration, startTime);
        this.parentTaskId = parentTaskId;
    }

    public SubTask(String name,
                   String description,
                   Status status,
                   long duration,
                   LocalDateTime startTime,
                   int parentTaskId) {
        super(name, description, status, duration, startTime);
        this.parentTaskId = parentTaskId;
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(int parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return id + "," + TaskType.SUBTASK + "," + name
                + "," + status + "," + description + ","  + duration + "," + startTime + "," + parentTaskId ;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        SubTask otherTask = (SubTask) obj;
        return this.id == otherTask.id && this.name.equals(otherTask.name)
                && this.description.equals(otherTask.description) && this.parentTaskId == otherTask.parentTaskId;
    }
}
