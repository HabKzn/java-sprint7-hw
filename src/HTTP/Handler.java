package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    HttpResponse<String> response;
    HttpExchange exchange;
    String[] pathSplitted;
    TaskManager manager;
    String path;

    public Handler(final TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        this.path = requestURI.toString().toLowerCase();
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
        if (path.equals("/tasks") || path.toLowerCase().equals("/tasks/")) {
            exchange.sendResponseHeaders(200, 0);
            String a = "запрос /tasks работает верно";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(a.getBytes());
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
                    String a = "запрос /tasks/history работает верно";
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(a.getBytes());
                    }

                    //                List<Task> history = manager.history();
//               Gson gson = new Gson();
//                String historyJson = gson.toJson(history);
//                try (OutputStream os = exchange.getResponseBody()) {
//                    os.write(historyJson.getBytes());
//                    exchange.close();
//                }
                    break;

                default:
                    exchange.sendResponseHeaders(405, 0);
            }
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

        if (pathSplitted.length == 3) {
           sendPositiveresponse("/tasks/task");
        } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
            {
                StringBuilder sb = new StringBuilder(pathSplitted[3]);
                sb.delete(0, 4);
                if (isDigit(sb.toString())) {
                    System.out.println(Integer.parseInt(sb.toString()));
                    sendPositiveresponse("/tasks/task/?id=");
                } else {
                   sendNegativeResponse();
                }
            }


            exchange.sendResponseHeaders(200, 0);
            String a = "запрос /tasks/task/?id= работает верно";
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(a.getBytes());

            }
//        String thirdElementOfPath = pathSplitted[3];
//
//        if ("".equals(thirdElementOfPath)) {
//            manager.getAllTasksList();
//            exchange.sendResponseHeaders(200, 0);
//        } else {
//            int id = extractId(thirdElementOfPath);
//            if (id == -1) {
//                exchange.sendResponseHeaders(405, 0);
//            } else {
//                exchange.sendResponseHeaders(200, 0);
//                Task task = manager.getTaskByUin(id);
//                String taskSerialized = gson.toJson(task);
//                try (OutputStream os = exchange.getResponseBody()) {
//                    os.write(taskSerialized.getBytes());
//                    exchange.close();
            //  }}}
        }
    }



    void taskDELETEhandle() throws IOException {

        String thirdElementOfPath = pathSplitted[3];

        if ("".equals(thirdElementOfPath)) {
            manager.getAllTasksList();
            exchange.sendResponseHeaders(200, 0);
           manager.clearTasks();
        } else {
//            int id = extractId(thirdElementOfPath);
//            if (id == -1) {
//                exchange.sendResponseHeaders(405, 0);
//            } else {
//                exchange.sendResponseHeaders(200, 0);
//              manager.deleteTaskById(id);
//            }
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

    void subTaskGEThandle() throws IOException {
        if (pathSplitted.length == 3) {
            sendPositiveresponse("/tasks/subtask");
        } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
                StringBuilder sb = new StringBuilder(pathSplitted[3]);
                sb.delete(0, 4);
                if (isDigit(sb.toString())) {
                    System.out.println(Integer.parseInt(sb.toString()));
                    sendPositiveresponse("/tasks/subtask/?id=");
                } else {
                    sendNegativeResponse();
                }
    }else if(pathSplitted.length == 5 && pathSplitted[4].startsWith("?id=") &&pathSplitted[3].equals("epic")){
            StringBuilder sb = new StringBuilder(pathSplitted[4]);
            sb.delete(0, 4);
            if (isDigit(sb.toString())) {
                System.out.println(Integer.parseInt(sb.toString()));
                sendPositiveresponse("/tasks/subtask/epic/?id=");
            } else {
                sendNegativeResponse();
            }

        }
    }

    void subTaskDELETEhandle() {
    }

    void subTaskPOSThandle() {
    }

    void epicGEThandle() throws IOException {
                  if (pathSplitted.length == 3) {
                sendPositiveresponse("/tasks/epic");
            } else if (pathSplitted.length == 4 && pathSplitted[3].startsWith("?id=")) {
                {
                    StringBuilder sb = new StringBuilder(pathSplitted[3]);
                    sb.delete(0, 4);
                    if (isDigit(sb.toString())) {
                        System.out.println(Integer.parseInt(sb.toString()));
                        sendPositiveresponse("/tasks/epic/?id=");
                    } else {
                        sendNegativeResponse();
                    }
                }
                exchange.sendResponseHeaders(200, 0);
                String a = "запрос /tasks/epic/?id= работает верно";
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(a.getBytes());
                }
            }
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

    void sendPositiveresponse(String path) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        String response = "Запрос " + path + " работает верно";
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    void sendNegativeResponse() throws IOException {
        exchange.sendResponseHeaders(405, 0);
    }
}
