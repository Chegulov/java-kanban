package com.chegulov.tasktracker.client;

import com.chegulov.tasktracker.server.KVServer;
import com.chegulov.tasktracker.service.exceptions.ServerSaveLoadException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KVTaskClientTest {
    KVTaskClient kvTaskClient;
    KVServer kvServer;

    @Test
    void shouldThrowExceptionIfWrongRegistration() {
        final ServerSaveLoadException exception = assertThrows(
                ServerSaveLoadException.class,
                () -> kvTaskClient = new KVTaskClient("http://localhost:8088/")
        );

        assertEquals(exception.getMessage(), "Регистрация неудачна");
    }

    @Test
    void shouldThrowExceptionIfWrongStatCodeWhenRegister() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        final ServerSaveLoadException exception = assertThrows(
                ServerSaveLoadException.class,
                () -> kvTaskClient = new KVTaskClient("http://localhost:8078/12345")

        );
        kvServer.stop();

        assertEquals(exception.getMessage(), "Некорректный ответ сервера: 404");
    }

    @Test
    void shouldThrowExceptionIfWrongStatCodeWhenPut() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
        final ServerSaveLoadException exception = assertThrows(
                ServerSaveLoadException.class,
                () -> kvTaskClient.put("1", "")
        );
        kvServer.stop();

        assertEquals(exception.getMessage(), "Некорректный ответ сервера: 400");
    }

    @Test
    void shouldThrowExceptionIfWrongPut() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
        kvServer.stop();
        final ServerSaveLoadException exception = assertThrows(
                ServerSaveLoadException.class,
                () -> kvTaskClient.put("1", "12321")
        );

        assertEquals(exception.getMessage(), "Запрос на сохранение удался");
    }

    @Test
    void shouldThrowExceptionIfWrongStatCodeWhenLoad() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        kvTaskClient = new KVTaskClient("http://localhost:8078/");
        final ServerSaveLoadException exception = assertThrows(
                ServerSaveLoadException.class,
                () -> kvTaskClient.load("")
        );
        kvServer.stop();

        assertEquals(exception.getMessage(), "Некорректный ответ сервера: 400 загрузка не удалась!");
    }
}
