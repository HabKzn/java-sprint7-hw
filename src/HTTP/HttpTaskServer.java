package HTTP;

import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    final int PORT = 8080;
    private TaskManager manager;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) {
        try {
            this.manager = manager;
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TasksHandler(manager));
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера");
            e.printStackTrace();
        }
    }

    public int getPORT() {
        return PORT;
    }

    public TaskManager getManager() {
        return manager;
    }

    public HttpServer getServer() {
        return server;
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

}