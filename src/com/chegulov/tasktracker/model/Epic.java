package com.chegulov.tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {

    private Map<Integer, SubTask> subTasks;
    private LocalDateTime endTime;

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
        countTime();
    }

    public void addSubTask (int id, SubTask subTask) {
        subTasks.put(id,subTask);
        status = determineStatus();
        countTime();
    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
        status = determineStatus();
        countTime();
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

    private void countTime() {
        if (!subTasks.isEmpty()) {
            for (SubTask subTask : subTasks.values()) {
                if (subTask.getEndTime() != null ) {
                    if (endTime == null || subTask.getEndTime().isAfter(endTime)) {
                        endTime = subTask.getEndTime();
                    }
                }
                if (subTask.getStartTime() != null) {
                    if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                        startTime = subTask.getStartTime();
                    }
                }
            }
            if (startTime != null && endTime != null) {
                duration = Duration.between(startTime, endTime).toMinutes();
            }
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return id + "," + TaskType.EPIC + "," + name + ","
                + status + "," + description + "," + duration + "," + startTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherTask = (Epic) obj;
        return this.id == otherTask.id && this.name.equals(otherTask.name)
                && this.description.equals(otherTask.description);
    }
}
