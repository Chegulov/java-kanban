package com.chegulov.tasktracker.server.handlers;

import com.chegulov.tasktracker.adapters.LocalDateTimeAdapter;
import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

//TODO добавить исключения при ошибках добавления, обновления задач и обработать здесь
public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    Gson gson;

    public TaskHandler(TaskManager taskManager) {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Началась обработка /tasks запроса от клиента.");
        URI uri = exchange.getRequestURI();
        Endpoint endpoint = getEndPoint(uri.getPath(), exchange.getRequestMethod());
        String query = uri.getQuery();
        int statusCode;
        String response;

        int id = -1;
        if (query != null) {
            try {
                id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
            } catch (StringIndexOutOfBoundsException e) {
                statusCode = 400;
                response = "В запросе отсутствует параметр id";

                sendResponse(exchange, statusCode, response);
            } catch (NumberFormatException e) {
                statusCode = 400;
                response = "Неверный формат id";

                sendResponse(exchange, statusCode, response);
            }
        }

        switch (endpoint) {
            case GET_TASK:
                if (query == null) {
                    getTasks(exchange);
                } else {
                    getTaskById(exchange, id);
                }
                break;
            case GET_SUBTASK:
                if (query == null) {
                    getSubTasks(exchange);
                } else {
                    getSubTaskById(exchange, id);
                }
                break;
            case GET_EPIC:
                if (query == null) {
                    getEpics(exchange);
                } else {
                    getEpicById(exchange, id);
                }
                break;
            case GET_HISTORY:
                getHistory(exchange);
                break;
            case GET:
                getPrioritizedTask(exchange);
                break;
            case GET_EPIC_SUB:
                getSubTasksByEpic(exchange, id);
                break;
            case POST_TASK:
                String taskJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
                    addTask(exchange, taskJson);
                } else {
                    updateTask(exchange, taskJson, id);
                }
                break;
            case POST_EPIC:
                String epicJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
                    addEpic(exchange, epicJson);
                } else {
                    updateEpic(exchange, epicJson, id);
                }
                break;
            case POST_SUBTASK:
                String subTaskJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
                    addSubTask(exchange, subTaskJson);
                } else {
                    updateSubTask(exchange, subTaskJson, id);
                }
                break;
            case DELETE_TASK:
                if (query == null) {
                    clearTasks(exchange);
                } else {
                    deleteTaskById(exchange, id);
                }
                break;
            case DELETE_SUBTASK:
                if (query == null) {
                    clearSubTasks(exchange);
                } else {
                    deleteSubTaskById(exchange, id);
                }
                break;
            case DELETE_EPIC:
                if (query == null) {
                    clearEpics(exchange);
                } else {
                    deleteEpicById(exchange, id);
                }
                break;
            default:
                statusCode = 503;
                response = "Что-то пошло не так";
                sendResponse(exchange, statusCode, response);
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String response = gson.toJson(taskManager.getTasks());
        sendResponse(exchange, statusCode, response);
    }

    private void getTaskById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        Task task = taskManager.getTaskById(id);
        if (task != null) {
            statusCode = 200;
            response = gson.toJson(task);
        } else {
            statusCode = 404;
            response = "задача с данным id не найдена";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String response = gson.toJson(taskManager.getEpicTasks());
        sendResponse(exchange, statusCode, response);
    }

    private void getEpicById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;
        Epic epic = taskManager.getEpicTaskById(id);
        if (epic != null) {
            statusCode = 200;
            response = gson.toJson(epic);
        } else {
            statusCode = 404;
            response = "эпик с данным id не найден";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void getSubTasks(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String response = gson.toJson(taskManager.getSubTasks());
        sendResponse(exchange, statusCode, response);
    }

    private void getSubTaskById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        SubTask subTask = taskManager.getSubTaskById(id);
        if (subTask != null) {
            statusCode = 200;
            response = gson.toJson(subTask);
        } else {
            statusCode = 404;
            response = "подзадача с данным id не найдена";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String response = gson.toJson(taskManager.getHistory());
        sendResponse(exchange, statusCode, response);
    }

    private void getPrioritizedTask(HttpExchange exchange) throws IOException {
        int statusCode = 200;
        String response = gson.toJson(taskManager.getPrioritizedTasks());
        sendResponse(exchange, statusCode, response);
    }

    private void getSubTasksByEpic(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        Epic epic = taskManager.getEpicTaskById(id);
        if (epic != null) {
            statusCode = 200;
            response = gson.toJson(taskManager.getSubTasksByEpic(id));
        } else {
            statusCode = 404;
            response = "эпик с данным id не найден";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void addTask(HttpExchange exchange, String taskJson) throws IOException {
        int statusCode;
        String response;

        try {
            Task task = gson.fromJson(taskJson, Task.class);
            boolean b = taskManager.addTask(task);
            if (b) {
                statusCode = 201;
                response = "Задача добавлена с id= " + task.getId();
            } else {
                statusCode = 400;
                response = "Задача не добавлена, некорректный запрос";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void updateTask(HttpExchange exchange, String taskJson, int id) throws IOException {
        int statusCode;
        String response;

        try {
            Task task = gson.fromJson(taskJson, Task.class);
            if (taskManager.getTaskById(id) != null) {
                boolean b = taskManager.addTask(task);
                if (b) {
                    taskManager.updateTask(id, task);
                    statusCode = 201;
                    response = "Задача обновлена с id= " + task.getId();
                } else {
                    statusCode = 400;
                    response = "Задача не обновлена, некорректный запрос";
                }
            } else {
                statusCode = 404;
                response = "Задача с таким id не найдена";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void addEpic(HttpExchange exchange, String epicJson) throws IOException {
        int statusCode;
        String response;

        try {
            Epic epicPost = gson.fromJson(epicJson, Epic.class);
            if (epicPost.getSubTasks() == null) {
                epicPost.setSubTasks(new ArrayList<>());
            }
            boolean b = taskManager.addEpicTask(epicPost);
            if (b) {
                statusCode = 201;
                response = "Эпик добавлен с id= " + epicPost.getId();
            } else {
                statusCode = 400;
                response = "Эпик не добавлен, некорректный запрос";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void updateEpic(HttpExchange exchange, String epicJson, int id) throws IOException {
        int statusCode;
        String response;

        try {
            Epic epicPost = gson.fromJson(epicJson, Epic.class);
            if (taskManager.getEpicTaskById(id) != null) {
                boolean b = taskManager.addEpicTask(epicPost);
                if (b) {
                    taskManager.updateEpicTask(id, epicPost);
                    statusCode = 201;
                    response = "Эпик обновлен с id= " + epicPost.getId();
                } else {
                    statusCode = 400;
                    response = "Эпик не обновлен, некорректный запрос";
                }
            } else {
                statusCode = 404;
                response = "Эпик с таким id не найден";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void addSubTask(HttpExchange exchange, String subTaskJson) throws IOException {
        int statusCode;
        String response;

        try {
            SubTask subTaskPost = gson.fromJson(subTaskJson, SubTask.class);
            boolean b = taskManager.addSubTask(subTaskPost);
            if (b) {
                statusCode = 201;
                response = "Подзадача добавлена с id= " + subTaskPost.getId();
            } else {
                statusCode = 400;
                response = "Подзадача не добавлена, некорректный запрос";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void updateSubTask(HttpExchange exchange, String subTaskJson, int id) throws IOException {
        int statusCode;
        String response;

        try {
            SubTask subTaskPost = gson.fromJson(subTaskJson, SubTask.class);
            if (taskManager.getSubTaskById(id) != null) {
                boolean b = taskManager.updateSubTask(id, subTaskPost);
                if (b) {
                    statusCode = 201;
                    response = "Подзадача обновлена с id= " + subTaskPost.getId();
                } else {
                    statusCode = 400;
                    response = "Подзадача не добавлена, некорректный запрос";
                }
            } else {
                statusCode = 404;
                response = "Подзадача с таким id не найдена";
            }
        } catch (JsonSyntaxException e) {
            statusCode = 400;
            response = "некорректный формат JSON";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void clearTasks(HttpExchange exchange) throws IOException {
        taskManager.clearTasks();
        int statusCode = 200;
        String response = "Все задачи удалены";
        sendResponse(exchange, statusCode, response);
    }

    private void deleteTaskById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        Task task = taskManager.getTaskById(id);
        if (task != null) {
            taskManager.removeTask(id);
            statusCode = 200;
            response = "Задача с id=" + id + " удалена";
        } else {
            statusCode = 404;
            response = "Задача с данным id не найдена";
        }
        sendResponse(exchange, statusCode, response);
    }

    private void clearSubTasks(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        taskManager.clearSubTasks();
        statusCode = 200;
        response = "Все подзадачи удалены";
        sendResponse(exchange, statusCode, response);
    }

    private void deleteSubTaskById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        SubTask subTask = taskManager.getSubTaskById(id);
        if (subTask != null) {
            taskManager.removeSubTask(id);
            statusCode = 200;
            response = "Подзадача с id=" + id + " удалена";
        } else {
            statusCode = 404;
            response = "Подзадача с данным id не найдена";
        }
        sendResponse(exchange, statusCode, response);
    }
    private void clearEpics(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        taskManager.clearEpicTasks();
        statusCode = 200;
        response = "Все эпики удалены";
        sendResponse(exchange, statusCode, response);
    }

    private void deleteEpicById(HttpExchange exchange, int id) throws IOException {
        int statusCode;
        String response;

        Epic epicDel = taskManager.getEpicTaskById(id);
        if (epicDel != null) {
            taskManager.removeEpicTask(id);
            statusCode = 200;
            response = "Эпик с id=" + id + " удалён";
        } else {
            statusCode = 404;
            response = "Эпик с данным id не найден";
        }
        sendResponse(exchange, statusCode, response);
    }

    private Endpoint getEndPoint(String path, String method) {
        String[] pathParts = path.split("/");
        String result = method;
        if (pathParts.length > 3 && pathParts[3].equals("epic")) {
            result = method + "_EPIC_SUB";
        } else if (pathParts.length > 2) {
            result = method + "_" + pathParts[2];
        }
        try {
            return Endpoint.valueOf(result.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Endpoint.UNKNOWN;
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
