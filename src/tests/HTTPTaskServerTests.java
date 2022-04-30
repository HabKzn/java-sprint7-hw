package tests;

import ServersAndClient.HttpTaskServer;
import ServersAndClient.KVServer;
import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPTaskServerTests {
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    TaskManager manager;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }


    void GETTasksWhileEmptyList() {
        sendGetResponseTo("tasks/task/");
    }

    void GETTasksWhileNotEmptyList() {
        sendGetResponseTo("tasks/task/");
    }

    void GETSubTasksWhileEmptyList() {
        sendGetResponseTo("tasks/subtask/");
    }

    void GETSubTasksWhileNotEmptyList() {
        sendGetResponseTo("tasks/subtask/");
    }

    void GETEpicsWhileEmptyList() {
        sendGetResponseTo("tasks/epic/");
    }

    void GETEpicsTaskWhileNotEmptyList() {
        sendGetResponseTo("tasks/epic/");
    }

    void GETTasksByIdWhileEmptyList() {
        sendGetResponseTo("tasks/task/?id=1");
    }

    void GETTasksByIdWhileNotEmptyList() {
        sendGetResponseTo("tasks/task/?id=1");
    }

    void GETSubTasksByIdWhileEmptyList() {
        sendGetResponseTo("tasks/subtask/?id=1");
    }

    void GETSubTasksByIdWhileNotEmptyList() {
        sendGetResponseTo("tasks/subtask/?id=1");
    }

    void GETEpicsByIdWhileEmptyList() {
        sendGetResponseTo("tasks/epic/?id=1");
    }

    void GETEpicsByIdWhileNotEmptyList() {
        sendGetResponseTo("tasks/epic/?id=1");
    }

    void GEThistoryWhileEmpty() {
        sendGetResponseTo("tasks/history");
    }

    void GEThistory() {
        sendGetResponseTo("tasks/history");
    }

    void GETTasksPriority() {
        sendGetResponseTo("tasks/");
    }

    void GETTasksPriorityWhileEmpty() {
        sendGetResponseTo("tasks/");
    }

    void POSTTask() {
       Task task = null;
        sendPostResponse("task", task);
    }

    void POSTEpic() {
        Epic epic = null;
        sendPostResponse("task", epic);
    }

    void POSTSubTask() {
        SubTask subtask = null;
        sendPostResponse("task", subtask);
    }

    void DeleteTasks() {
        sendPostResponse();
    }

    void DeleteEpics() {
        sendPostResponse();
    }

    void DeleteSubTasks() {
        sendPostResponse();
    }

    void DeleteTask() {
    }

    void DeleteSubTask() {
    }

    void DeleteEpic() {
    }


    void sendGetResponseTo(String path) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    void sendPostResponse(String postPath, Task newTask) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/" + postPath);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        Gson gson = new Gson();
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
