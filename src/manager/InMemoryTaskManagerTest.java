package manager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        filledManager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();

        Task task = new Task("name", "description");  //id = 3
        Epic epic = new Epic("epicName", "epicDescription");  // id = 1
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic); //id = 2

        filledManager.createEpic(epic);
        filledManager.createSubTask(subTask, epic);
        filledManager.createTask(task);
    }
}