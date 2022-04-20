package HTTP;

import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    static final int PORT = 8080;
    public static TaskManager manager;
    public static HttpServer httpServer;

    public HttpTaskServer() {
        manager = (FileBackedTasksManager)Managers.getFileBacked();

        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера");
            e.printStackTrace();
        }

        httpServer.createContext("/tasks", new TasksHandler());
    }


}