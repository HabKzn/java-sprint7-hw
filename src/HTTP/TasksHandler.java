package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class TasksHandler implements HttpHandler {

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        String[] pathSplitted = path.split("/");

        switch (method) {
            case "GET":
                getHandler(pathSplitted);
                break;

            case "DELETE":
                deleteHandler(pathSplitted);
                break;

            case "POST":
                postHandler(pathSplitted);
                break;

            default:
                exchange.sendResponseHeaders(405, 0);
        }
    }


    void getHandler(String[] pathSplitted) {

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

            case "":
                HttpTaskServer.manager.getPrioritizedTask();
                break;

            case "history":
                HttpTaskServer.manager.history();
                break;
        }

    }









    void taskGEThandle() {
    }

    void taskDELETEhandle() {
    }

    void taskPOSThandle() {
    }

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
}
