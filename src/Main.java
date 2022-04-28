import HTTP.HttpTaskServer;
import manager.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
   //     FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("memoryFile.csv"));
 // HttpTaskServer server = new HttpTaskServer(manager);
    //    server.start();
        new KVServer().start();
    }
}

