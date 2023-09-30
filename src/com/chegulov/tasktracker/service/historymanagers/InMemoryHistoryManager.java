package com.chegulov.tasktracker.service.historymanagers;

import com.chegulov.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int size = 0;
    private final Map<Integer,Node> historyHelper;

    public InMemoryHistoryManager() {
        historyHelper = new HashMap<>();
    }
    @Override
    public List<Task> getHistory() {
        if (head != null) {
            return getTasks();
        }
        return new ArrayList<>();
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>(size);
        Node iter = head;
        for (int i = 1; i <= size; i++) {
            history.add(iter.getData());
            iter = iter.getNext();
        }
        return history;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyHelper.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        historyHelper.put(task.getId(),tail);
    }
    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(tail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.setNext(newNode);
        size++;
    }

    @Override
    public void remove(int id) {
        if (!historyHelper.containsKey(id)) {
            return;
        }
        removeNode(historyHelper.get(id));

    }

    private void removeNode(Node node) {
        Node prev = node.getPrev();
        Node next = node.getNext();
        if (next == null && prev == null) {
            head = null;
            tail = null;
            size--;
            return;
        }
        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
        }
        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
        }
        size--;
    }
}
