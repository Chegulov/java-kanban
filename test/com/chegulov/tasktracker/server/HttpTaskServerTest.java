package com.chegulov.tasktracker.server;

import com.chegulov.tasktracker.adapters.LocalDateTimeAdapter;
import com.chegulov.tasktracker.model.Epic;
import com.chegulov.tasktracker.model.Status;
import com.chegulov.tasktracker.model.SubTask;
import com.chegulov.tasktracker.model.Task;
import com.chegulov.tasktracker.service.taskmanagers.HttpTaskManager;
import com.chegulov.tasktracker.service.taskmanagers.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private KVServer kvServer;
    private static final String url = "http://localhost:8078/";
    private HttpClient client;
    private URI uri;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private Epic epic;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;
    private Task task;
    private Task task2;
    private Task task3;

    private void init() {
        epic = new Epic("epicTest", "sb1 sb2");
        epic2 = new Epic("epicTest2", "testing");
        subTask1 = new SubTask("Sb1", "test1", 1);
        subTask2 = new SubTask("Sb2", "test2", Status.DONE, 1);
        task = new Task("Task1", "task");
        task2 = new Task("Task2", "task2");
        task3 = new Task("Task3", "task3");
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        init();
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HttpTaskManager(url);
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void shouldPostTask() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(task, taskManager.getTaskById(1));
        assertEquals("Задача добавлена с id= 1", response.body());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        json = gson.toJson(task2);
        task2.setId(1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(task2, taskManager.getTaskById(1));
        assertEquals("Задача обновлена с id= 1", response.body());
    }

    @Test
    void shouldPostEpic() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(epic, taskManager.getEpicTaskById(1));
        assertEquals("Эпик добавлен с id= 1", response.body());
    }

    @Test
    void shouldUpdateEpic() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        json = gson.toJson(epic2);
        epic2.setId(1);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(epic2, taskManager.getEpicTaskById(1));
        assertEquals("Эпик обновлен с id= 1", response.body());
    }

    @Test
    void shouldPostSubTask() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(subTask1, taskManager.getSubTaskById(2));
        assertEquals("Подзадача добавлена с id= 2", response.body());
    }

    @Test
    void shouldUpdateSubTask() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        json = gson.toJson(subTask2);
        subTask2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(subTask2, taskManager.getSubTaskById(2));
        assertEquals("Подзадача обновлена с id= 2", response.body());
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(task2);
        task2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Task> testMap = new HashMap<>();
        testMap.put(1, task);
        testMap.put(2, task2);
        json = gson.toJson(testMap);

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(json, response.body());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(task, gson.fromJson(response.body(), Task.class));
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(epic2);
        epic2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, Epic> testMap = new HashMap<>();
        testMap.put(1, epic);
        testMap.put(2, epic2);
        json = gson.toJson(testMap);

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(json, response.body());
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        client = HttpClient.newHttpClient();
        uri = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(epic, gson.fromJson(response.body(), Epic.class));
    }

    @Test
    void shouldGetSubTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(subTask2);
        subTask2.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        HashMap<Integer, SubTask> testMap = new HashMap<>();
        testMap.put(2, subTask1);
        testMap.put(3, subTask2);
        json = gson.toJson(testMap);

        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(json, response.body());
    }

    @Test
    void shouldGetSubTaskById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(subTask1, gson.fromJson(response.body(), SubTask.class));
    }

    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(task2);
        task2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(taskManager.getTasks().isEmpty());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(task2);
        task2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=2");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNull(taskManager.getTaskById(2));
        assertNotNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldDeleteEpics() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(epic2);
        epic2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(taskManager.getEpicTasks().isEmpty());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(epic2);
        epic2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNull(taskManager.getTaskById(2));
        assertNotNull(taskManager.getEpicTaskById(1));
    }

    @Test
    void shouldDeleteSubTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(subTask2);
        subTask2.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(taskManager.getSubTasks().isEmpty());
    }

    @Test
    void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(subTask2);
        subTask2.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNull(taskManager.getSubTaskById(2));
        assertNotNull(taskManager.getSubTaskById(3));
    }

    @Test
    void shouldGetEpicSubTasks() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        epic.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subTask1);
        subTask1.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        json = gson.toJson(subTask2);
        subTask2.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> testList = new ArrayList<>();
        testList.add(subTask1);
        testList.add(subTask2);
        json = gson.toJson(testList);

        assertEquals(json, response.body());
    }

    @Test
    void shouldGetPrioritizedTask() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        task = new Task("тасквремя", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 20));
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        task2 = new Task("таск2время", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 0));
        json = gson.toJson(task2);
        task2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        task3 = new Task("таск3время", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 40));
        json = gson.toJson(task3);
        task3.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        TreeSet<Task> testSet = new TreeSet<>(Comparator.comparing(
                Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId));
        testSet.add(task);
        testSet.add(task2);
        testSet.add(task3);
        json = gson.toJson(testSet);

        uri = URI.create("http://localhost:8080/tasks/");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(json, response.body());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        uri = URI.create("http://localhost:8080/tasks/task/");
        task = new Task("тасквремя", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 20));
        String json = gson.toJson(task);
        task.setId(1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        task2 = new Task("таск2время", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 0));
        json = gson.toJson(task2);
        task2.setId(2);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        task3 = new Task("таск3время", "666", 20, LocalDateTime.of(2000, 1, 1, 0, 40));
        json = gson.toJson(task3);
        task3.setId(3);
        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=3");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        uri = URI.create("http://localhost:8080/tasks/task/?id=2");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> testList = new ArrayList<>(List.of(task,task3,task2));
        json = gson.toJson(testList);

        uri = URI.create("http://localhost:8080/tasks/history");
        request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(json, response.body());
    }
}
