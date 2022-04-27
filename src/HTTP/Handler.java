package HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Handler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    HttpResponse<String> response;
    HttpExchange exchange;
    String[] pathSplitted;
    TaskManager manager;
    String path;
    String body;
    Gson gson;

    public Handler(final TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        gson = new Gson();
        this.exchange = exchange;
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        this.path = requestURI.toString().toLowerCase();
        pathSplitted = path.split("/");
        InputStream is = exchange.getRequestBody();
        body = new String(is.readAllBytes(), DEFAULT_CHARSET);


        try {
            switch (method) {
                case "GET":
                    getHandler();
                    break;

                case "DELETE":
                    deleteHandler();
                    break;

                case "POST":
                    postHandler();
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(405, 0);
        }
    }


    void getHandler() throws IOException {
        if (path.equals("/tasks") || path.toLowerCase().equals("/tasks/")) {
            exchange.sendResponseHeaders(200, 0);
            gson = new GsonBuilder().create();
            String prioritizedJson = gson.toJson(manager.getPrioritizedTask());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(prioritizedJson.getBytes());
            }
        } else {
            switch (pathSplitted[2].toLowerCase()) {

                case "task":
                    taskGEThandle();
                    break;

                case "subtask":
                    subTaskGEThandle();
                    break;

                case "epic":
                    epicGEThandle();
                    break;

                case "history":
                    exchange.sendResponseHeaders(200, 0);
                    List<Task> history = manager.history();
                    gson = new GsonBuilder().create();
                    String historyJson = gson.toJson(history);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(historyJson.getBytes());
                        exchange.close();
                    }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
        }
    }

    void deleteHandler() throws IOException {
        switch (pathSplitted[2]) {

            case "task":
                taskDELETEhandle();
                ;
                break;

            case "subtask":
                subTaskDELETEhandle();
                break;

            case "epic":
                epicDELETEhandle();
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
        }
    }

    void postHandler() throws IOException {
        switch (pathSplitted[2].toLowerCase()) {
            case "task":
                taskPOSThandler();
                break;

            case "subtask":
                subTaskPOSThandle();
                break;

            case "epic":
                epicPOSThandle();
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
        }
    }

    void taskGEThandle() throws IOException {
        GsonBuilder gbuilder = new GsonBuilder().setPrettyPrinting();
        gson = gbuilder.create();
        if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String js = gson.toJson(manager.getAllTasksList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(js.getBytes());
                exchange.close();
            }
        } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                int id = Integer.parseInt(sb.toString());
                if (manager.getManagerTasksMap().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getTaskByUin(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        } else exchange.sendResponseHeaders(400, 0);
    }

    void taskPOSThandler() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        Task task = gson.fromJson(body, Task.class);
        manager.createTask(task);
        manager.updateTask(task);
    }


    void taskDELETEhandle() throws IOException {
        if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                int id = Integer.parseInt(sb.toString());
                manager.deleteTaskById(id);

            } else exchange.sendResponseHeaders(400, 0);
        } else if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearTasks();
        } else
            exchange.sendResponseHeaders(400, 0);
    }

    void subTaskPOSThandle() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        SubTask subTask = gson.fromJson(body, SubTask.class);
        manager.createTask(subTask);
        manager.updateTask(subTask);
    }

    void epicPOSThandle() throws IOException {
        exchange.sendResponseHeaders(200, 0);
        Epic epic = gson.fromJson(body, Epic.class);
        manager.createTask(epic);
        manager.updateTask(epic);
    }

    void subTaskGEThandle() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String js = gson.toJson(manager.getAllSubTasksList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(js.getBytes());
                exchange.close();
            }

        } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                int id = Integer.parseInt(sb.toString());
                if (manager.getManagerSubTasksMap().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getSubTaskByUin(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        } else if (pathSplitted.length == 5 && pathSplitted[4].startsWith("?id=") && pathSplitted[3].equals("epic")) {
            StringBuilder sb = new StringBuilder(pathSplitted[4]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                System.out.println(Integer.parseInt(sb.toString()));
                exchange.sendResponseHeaders(400, 0);
            } else {
                exchange.sendResponseHeaders(400, 0);
            }

        }
    }

    void subTaskDELETEhandle() throws IOException {
        if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                int id = Integer.parseInt(sb.toString());
                manager.deleteSubTaskById(id);

            } else exchange.sendResponseHeaders(400, 0);
        } else if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearSubTasks();
        } else
            exchange.sendResponseHeaders(400, 0);

    }


    void epicGEThandle() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            String js = gson.toJson(manager.getAllEpicsList());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(js.getBytes());
                exchange.close();
            }
        } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                int id = Integer.parseInt(sb.toString());
                if (manager.getManagerEpicsMap().containsKey(id)) {
                    exchange.sendResponseHeaders(200, 0);
                    Task task = manager.getEpicByUin(id);
                    String taskSerialized = gson.toJson(task);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(taskSerialized.getBytes());
                        exchange.close();
                    }
                } else exchange.sendResponseHeaders(405, 0);
            } else exchange.sendResponseHeaders(405, 0);
        }
    }

    void epicDELETEhandle() throws IOException {
        if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            StringBuilder sb = new StringBuilder(pathSplitted[3]);
            sb.delete(0, 4);
            if (isPositiveDigit(sb.toString())) {
                exchange.sendResponseHeaders(200, 0);
                int id = Integer.parseInt(sb.toString());
                manager.deleteEpicById(id);

            } else exchange.sendResponseHeaders(400, 0);
        } else if (pathSplitted.length == 3) {
            exchange.sendResponseHeaders(200, 0);
            manager.clearEpics();
        } else
            exchange.sendResponseHeaders(400, 0);
    }

    int extractId(String thirdElementOfPath) {
        String[] thirdElementSplitted = thirdElementOfPath.split("\\?id=");
        if (thirdElementSplitted[0].equals("") && isPositiveDigit(thirdElementSplitted[2])) {
            return Integer.parseInt(thirdElementSplitted[2]);
        } else return -1;
    }

    private static boolean isPositiveDigit(String s) throws NumberFormatException {
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
