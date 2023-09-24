package com.chegulov.tasktracker.tests;

import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.Status;
import com.chegulov.tasktracker.model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EpicTest {
    private static Epic epic;

    @BeforeEach
    public void beforeAll() {
        epic = new Epic("testName","testDescription");
        epic.setId(1);
    }

    @Test
    public void shouldStatusNewWhenSTEmpty() {
        SubTask subTask = new SubTask("subName", "subDescriprion", 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        epic.removeSubTask(2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldStatusNewWhenAllNew() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.NEW, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.NEW, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldStatusDoneWhenAllDone() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.DONE, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.DONE, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    public void shouldStatusInProgressWhenNewDone() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.NEW, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        SubTask subTask2 = new SubTask("sub2Name", "sub2Descriprion", Status.DONE, 1);
        subTask2.setId(3);
        epic.addSubTask(3, subTask2);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void shouldStatusInProgressWhenInProgress() {
        SubTask subTask = new SubTask("subName", "subDescriprion", Status.IN_PROGRESS, 1);
        subTask.setId(2);
        epic.addSubTask(2, subTask);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}