package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import tasks.Status;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void beforeEach() {
        filledManager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();
        filledManager.createTask(task);
        filledManager.createEpic(epic1);
        filledManager.createSubTask(subTask11, epic1.getId());
        filledManager.createSubTask(subTask12, epic1.getId());
        filledManager.createEpic(epic2);
        filledManager.createSubTask(subTask21, epic2.getId());
        subTask21.setStatus(Status.DONE);
        filledManager.updateSubTask(subTask21);
    }
}