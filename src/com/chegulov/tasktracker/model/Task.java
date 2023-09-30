package com.chegulov.tasktracker.model;

import java.time.LocalDateTime;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        return startTime != null ? startTime.plusMinutes(duration) : null;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + "," + TaskType.TASK + "," + name + ","
                + status + "," + description + "," + duration + "," + startTime;
    }

    @Override
    public int hashCode() {
        return id*11 + name.length()*7 + description.length();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return this.id == otherTask.id && this.name.equals(otherTask.name)
                && this.description.equals(otherTask.description);
    }
}
