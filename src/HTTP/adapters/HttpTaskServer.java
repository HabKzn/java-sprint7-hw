package HTTP.adapters;

import HTTP.handlers.MainHandler;
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
            server.createContext("/tasks", new MainHandler(manager));
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

    public static class EpicAdapter extends TypeAdapter<Epic> {

        @Override
        public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {

        }

        @Override
        public Epic read(final JsonReader jsonReader) throws IOException {
            return null;
        }
    }

    public static class ArrayListOfTasksAdapter extends TypeAdapter<ArrayList<Task>> {
        @Override
        public void write(final JsonWriter jsonWriter, final ArrayList<Task> tasks) throws IOException {

        }

        @Override
        public ArrayList<Task> read(final JsonReader jsonReader) throws IOException {
            return null;
        }
    }
}