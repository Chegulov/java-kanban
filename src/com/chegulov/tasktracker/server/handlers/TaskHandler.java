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

//TODO добавить исключения при ошибках добавления, обновления задач и обработать здесь
public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager) {
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
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        int id = -1;
        if (query != null) {
            try {
                id = Integer.parseInt(query.substring(query.indexOf("?id=") + 4));
            } catch (StringIndexOutOfBoundsException e) {
                statusCode = 400;
                response = "В запросе отсутствует параметр id";
            } catch (NumberFormatException e) {
                statusCode = 400;
                response = "Неверный формат id";
            }
        }

        switch (endpoint) {
            case GET_TASK:
                if (query == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getTasks());
                } else {
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        statusCode = 200;
                        response = gson.toJson(task);
                    } else {
                        statusCode = 404;
                        response = "задача с данным id не найдена";
                    }

                }
                break;
            case GET_SUBTASK:
                if (query == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getSubTasks());
                } else {
                    SubTask subTask = taskManager.getSubTaskById(id);
                    if (subTask != null) {
                        statusCode = 200;
                        response = gson.toJson(subTask);
                    } else {
                        statusCode = 404;
                        response = "подзадача с данным id не найдена";
                    }
                }
                break;
            case GET_EPIC:
                if (query == null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getEpicTasks());
                } else {
                    Epic epic = taskManager.getEpicTaskById(id);
                    if (epic != null) {
                        statusCode = 200;
                        response = gson.toJson(epic);
                    } else {
                        statusCode = 404;
                        response = "эпик с данным id не найден";
                    }
                }
                break;
            case GET_HISTORY:
                statusCode = 200;
                response = gson.toJson(taskManager.getHistory());
                break;
            case GET:
                statusCode = 200;
                response = gson.toJson(taskManager.getPrioritizedTasks());
                break;
            case GET_EPIC_SUB:
                Epic epic = taskManager.getEpicTaskById(id);
                if (epic != null) {
                    statusCode = 200;
                    response = gson.toJson(taskManager.getSubTaskMapByEpic(id));
                } else {
                    statusCode = 404;
                    response = "эпик с данным id не найден";
                }
                break;
            case POST_TASK:
                String taskJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
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
                } else {
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
                }
                break;
            case POST_EPIC:
                String epicJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
                    try {
                        Epic epicPost = gson.fromJson(epicJson, Epic.class);
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
                } else {
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
                }
                break;
            case POST_SUBTASK:
                String subTaskJson = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                if (query == null) {
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
                } else {
                    try {
                        SubTask subTaskPost = gson.fromJson(subTaskJson, SubTask.class);
                        if (taskManager.getSubTaskById(id) != null) {
                            boolean b = taskManager.updateSubTask(id, subTaskPost);
                            if (b) {
                                statusCode = 201;
                                response = "Подзадача добавлена с id= " + subTaskPost.getId();
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
                }
                break;
            case DELETE_TASK:
                if (query == null) {
                    taskManager.clearTasks();
                    statusCode = 200;
                    response = "Все задачи удалены";
                } else {
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        taskManager.removeTask(id);
                        statusCode = 200;
                        response = "Задача с id=" + id + " удалена";
                    } else {
                        statusCode = 404;
                        response = "Задача с данным id не найдена";
                    }

                }
                break;
            case DELETE_SUBTASK:
                if (query == null) {
                    taskManager.clearSubTasks();
                    statusCode = 200;
                    response = "Все подзадачи удалены";
                } else {
                    SubTask subTask = taskManager.getSubTaskById(id);
                    if (subTask != null) {
                        taskManager.removeSubTask(id);
                        statusCode = 200;
                        response = "Подзадача с id=" + id + " удалена";
                    } else {
                        statusCode = 404;
                        response = "Подзадача с данным id не найдена";
                    }
                }
                break;
            case DELETE_EPIC:
                if (query == null) {
                    taskManager.clearEpicTasks();
                    statusCode = 200;
                    response = "Все эпики удалены";
                } else {
                    Epic epicDel = taskManager.getEpicTaskById(id);
                    if (epicDel != null) {
                        taskManager.removeEpicTask(id);
                        statusCode = 200;
                        response = "Эпик с id=" + id + " удалён";
                    } else {
                        statusCode = 404;
                        response = "Эпик с данным id не найден";
                    }
                }
                break;
            default:
                statusCode = 503;
                response = "Что-то пошло не так";
        }

        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
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
}
