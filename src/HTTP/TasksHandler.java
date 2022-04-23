package HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

public class TasksHandler implements HttpHandler {
    HttpResponse<String> response;
    HttpExchange exchange;
    String[] pathSplitted;
    TaskManager manager;

    public TasksHandler(final TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("aaa");
        this.exchange = exchange;
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        pathSplitted = path.split("/");
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
    }


    void getHandler() throws IOException {
        if(pathSplitted.length < 3){
           exchange.sendResponseHeaders(200, 0);
           List<Task> tasksPrioritized = manager.getPrioritizedTask();
         Gson gson = new GsonBuilder()
                 .setPrettyPrinting()
                 .registerTypeAdapter(Task.class, new TaskAdapter())
                 .create();
            String tasksPrioritizedJson = gson.toJson(tasksPrioritized);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(tasksPrioritizedJson.getBytes());

            }
        }else{
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

//            case "history":
//                exchange.sendResponseHeaders(200, 0);
//                List<Task> history = manager.history();
//             //   String historyJson = gson.toJson(history);
//                try (OutputStream os = exchange.getResponseBody()) {
//                    os.write(historyJson.getBytes());
//                    exchange.close();
//                }
//                break;

            default:
                exchange.sendResponseHeaders(405, 0);}
        }
    }

    void deleteHandler() throws IOException {
        switch (pathSplitted[2].toLowerCase()) {

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

        String thirdElementOfPath = pathSplitted[3];

        if ("".equals(thirdElementOfPath)) {
            manager.getAllTasksList();
            exchange.sendResponseHeaders(200, 0);
        } else {
            int id = extractId(thirdElementOfPath);
            if (id == -1) {
                exchange.sendResponseHeaders(405, 0);
            } else {
//                exchange.sendResponseHeaders(200, 0);
//                Task task = manager.getTaskByUin(id);
//                String taskSerialized = gson.toJson(task);
//                try (OutputStream os = exchange.getResponseBody()) {
//                    os.write(taskSerialized.getBytes());
//                    exchange.close();
              //  }
            }
        }
    }


    void taskDELETEhandle() throws IOException {

        String thirdElementOfPath = pathSplitted[3];

        if ("".equals(thirdElementOfPath)) {
            manager.getAllTasksList();
            exchange.sendResponseHeaders(200, 0);
           manager.clearTasks();
        } else {
            int id = extractId(thirdElementOfPath);
            if (id == -1) {
                exchange.sendResponseHeaders(405, 0);
            } else {
                exchange.sendResponseHeaders(200, 0);
              manager.deleteTaskById(id);
            }
        }
    }



//    void taskPOSThandle() {
//    Task task;
//    InputStream is = exchange.getRequestBody();
//    String body = is.
//
//
//
//        HttpTaskServer.manager.createTask(task);
//        HttpTaskServer.manager.updateTask(task);
//
//
//
//
//
//
//
//
//    }

    void subTaskGEThandle() {
    }

    void subTaskDELETEhandle() {
    }

    void subTaskPOSThandle() {
    }

    void epicGEThandle() {
    }

    void epicDELETEhandle() {
    }

    void epicPOSThandle() {
    }

    void historyhandle() {
    }

    int extractId(String thirdElementOfPath) {
        String[] thirdElementSplitted = thirdElementOfPath.split("\\?id=");
        if (thirdElementSplitted[0].equals("") && isDigit(thirdElementSplitted[2])) {
            return Integer.parseInt(thirdElementSplitted[2]);
        } else return -1;
    }

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
