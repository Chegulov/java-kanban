package com.chegulov.tasktracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private static Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("testName","testDescription");
        epic.setId(1);
    }

    @Test
    void shouldStatusNewWhenSTEmpty() {
        SubTask subTask = new SubTask("subName", "subDescriprion", 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        epic.removeSubTask(2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void shouldStatusNewWhenAllNew() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.NEW, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.NEW, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void shouldStatusDoneWhenAllDone() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.DONE, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.DONE, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void shouldStatusInProgressWhenNewDone() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.NEW, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.DONE, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldStatusInProgressWhenInProgress() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.IN_PROGRESS, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void shouldCountTime() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.NEW,20, LocalDateTime.of(2000, 1, 1, 0, 0), 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.DONE,20, LocalDateTime.of(2000, 1, 1, 2, 0), 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        assertEquals(epic.getStartTime(), LocalDateTime.of(2000, 1, 1, 0, 0) );
        assertEquals(epic.getEndTime(), LocalDateTime.of(2000, 1, 1, 2, 20));
        assertEquals(epic.getDuration(),140);
    }
}