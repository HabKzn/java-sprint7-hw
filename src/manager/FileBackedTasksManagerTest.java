package manager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    @BeforeEach
    public void beforeEach(){

        filledManager = new FileBackedTasksManager("tests.csv");
        emptyManager = new FileBackedTasksManager("tests.csv");

        Task task = new Task("name", "description");
        Epic epic = new Epic("epicName", "epicDescription");
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);

        filledManager.createEpic(epic);
        filledManager.createSubTask(subTask, epic);
        filledManager.createTask(task);
    }
}