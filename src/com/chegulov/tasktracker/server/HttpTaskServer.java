package com.chegulov.tasktracker.server;

import com.chegulov.tasktracker.server.handlers.TaskHandler;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final  HttpServer server;
    private final int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        server.stop(0);
    }
}
