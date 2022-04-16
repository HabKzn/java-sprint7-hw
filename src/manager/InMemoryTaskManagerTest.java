package manager;

import org.junit.jupiter.api.BeforeEach;
import tasks.Status;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void beforeEach() {
        filledManager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();

        filledManager.createTask(task); //id1
        filledManager.createEpic(epic1); //id2
        filledManager.createSubTask(subTask11, epic1); // id3
        filledManager.createSubTask(subTask12, epic1); //id4
        filledManager.createEpic(epic2); //id5
        filledManager.createSubTask(subTask21, epic2);//id 6
        subTask21.setStatus(Status.DONE);
        filledManager.updateSubTask(subTask21);
    }
}