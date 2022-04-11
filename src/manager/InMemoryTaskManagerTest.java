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


        Task task = new Task("name", "description");
        Epic epic = new Epic("epicName", "epicDescription");
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);

        filledManager.createEpic(epic);
        filledManager.createSubTask(subTask, epic);
        filledManager.createTask(task);
    }
}