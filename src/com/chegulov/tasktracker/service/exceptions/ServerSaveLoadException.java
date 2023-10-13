package com.chegulov.tasktracker.service.exceptions;

public class ServerSaveLoadException extends RuntimeException {
    public ServerSaveLoadException(String message) {
        super(message);
    }
}
