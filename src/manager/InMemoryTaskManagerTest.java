package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
String taskTestString;
String epic1TestString;
String subTask11TestString;
String subTask12TestString;
String epic2TestString;
String subTask21TestString;

    @BeforeEach
    public void beforeEach() {
        filledManager = new InMemoryTaskManager();
        emptyManager = new InMemoryTaskManager();

        Task task = new Task("name", "description", LocalDateTime.of(2022, 4, 17, 10, 0), Duration.ofMinutes(50));
        Epic epic1 = new Epic("epicName", "epicDescription");
        SubTask subTask11 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));
        SubTask subTask12 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 11, 0), Duration.ofMinutes(60));
        Epic epic2 = new Epic("epicName", "epicDescription");
        SubTask subTask21 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 13, 0), Duration.ofMinutes(60));


        filledManager.createTask(task);
        filledManager.createEpic(epic1);
        filledManager.createSubTask(subTask11, epic1);
        filledManager.createSubTask(subTask12, epic1);
        filledManager.createEpic(epic2);
        filledManager.createSubTask(subTask21, epic2);

        System.out.println();

    }

    @Test
    void Test() {

    }
}