package com.chegulov.tasktracker.server.handlers;

public enum Endpoint {
    GET_TASK, GET_SUBTASK, GET_EPIC,
    POST_TASK, POST_SUBTASK, POST_EPIC,
    DELETE_TASK, DELETE_SUBTASK, DELETE_EPIC,
    GET, GET_HISTORY, GET_EPIC_SUB, UNKNOWN
}
