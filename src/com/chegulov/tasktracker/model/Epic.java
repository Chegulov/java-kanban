package com.chegulov.tasktracker.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Integer> subTasks) {
        this.subTasks = subTasks;
    }

    public void addSubTask(int id) {
        if (!subTasks.contains(id)) {
            subTasks.add(id);
        }
    }

    public void removeSubTask(int id) {
        subTasks.remove(Integer.valueOf(id));
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
