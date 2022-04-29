package HTTP;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import tasks.Epic;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class HttpTaskServer {
    final int PORT = 8080;
    private TaskManager manager;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) {
        try {
            this.manager = manager;
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new Handler(manager));
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера");
            e.printStackTrace();
        }
    }
      public TaskManager getManager() {
        return manager;
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }


}