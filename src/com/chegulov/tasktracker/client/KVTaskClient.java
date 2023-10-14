package com.chegulov.tasktracker.client;

import com.chegulov.tasktracker.service.exceptions.ServerSaveLoadException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url;
    private final HttpClient httpClient;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    private String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ServerSaveLoadException("Некорректный ответ сервера: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ServerSaveLoadException("Регистрация неудачна");
        }
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ServerSaveLoadException("Некорректный ответ сервера: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ServerSaveLoadException("Запрос на сохранение удался");
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return "";
            }
            if (response.statusCode() != 200) {
                throw new ServerSaveLoadException("Некорректный ответ сервера: " + response.statusCode() + " загрузка не удалась!");
            }
            return response.body();
        } catch (IOException e) {
            return "";
        } catch (InterruptedException e) {
            throw new ServerSaveLoadException("Запрос на загрузку не удался");
        }
    }
}
