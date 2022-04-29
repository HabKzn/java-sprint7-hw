package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.IOException;
import java.net.URI;

public class Managers {
    public static TaskManager getDefault() {
       return new HTTPTaskManager(URI.create("http://localhost:8078"));
    }

    public static TaskManager getInMemoryManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBacked() {
        return new FileBackedTasksManager("memoryFile.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
