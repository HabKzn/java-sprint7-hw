package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    Task task;
    Epic epic1;
    Epic epic2;
    SubTask subTask11;
    SubTask subTask12;
    SubTask subTask21;

    @BeforeEach
    public void beforeEach() {

        filledManager = new FileBackedTasksManager("tests.csv");
        emptyManager = new FileBackedTasksManager("tests.csv");


        task = new Task("name", "description", LocalDateTime.of(2022, 4, 17, 10, 0), Duration.ofMinutes(50));
        epic1 = new Epic("epicName", "epicDescription");
        subTask11 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));
        subTask12 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 11, 0), Duration.ofMinutes(60));
        epic2 = new Epic("epicName", "epicDescription");
        subTask11 = new SubTask("subTaskName", "subTaskDescription", epic1,
                LocalDateTime.of(2022, 4, 16, 13, 0), Duration.ofMinutes(60));


        filledManager.createTask(task);
        filledManager.createEpic(epic1);
        filledManager.createSubTask(subTask11, epic1);
        filledManager.createSubTask(subTask12, epic1);
        filledManager.createEpic(epic1);
        filledManager.createSubTask(subTask21, epic2);
    }

    @Test
    void saveWhileEmptyTasksList() throws IOException {
        emptyManager.save();
        //При пустом списке задач все-равно создается строка с заглавием + пустая строка, поэтому разммер 2
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).size(), 2);
    }

    @Test
    void saveContainsEpicWithNoSubTasks() throws IOException {
        emptyManager.createEpic(epic1);
        emptyManager.save();
        String string = Files.readAllLines(Paths.get("tests.csv")).get(1);
        assertEquals(string, "1,EPIC,epicName,NEW,epicDescription");
    }

    @Test
    void saveContainsFilledHistory() throws IOException {
        //Оба теста выше содержат пустой список просмотров, поэтому третьим тестом я сделаю сохранение с заполненным
        // списком просмотров
        emptyManager.createEpic(epic1);
        emptyManager.getEpicByUin(1);
        emptyManager.save();
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(3), "1");
    }
}