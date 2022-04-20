package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    public static TaskManager getFileBacked(){
        return new FileBackedTasksManager("memoryFile.csv");
    }

    public static HistoryManager getDefaultHistory()
    {
        return new InMemoryHistoryManager();
    }
}
