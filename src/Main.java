import HTTP.HttpTaskServer;
import manager.FileBackedTasksManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("memoryFile.csv"));
  HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
    }
}

