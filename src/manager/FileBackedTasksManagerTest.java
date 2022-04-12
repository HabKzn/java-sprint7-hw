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

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    public void beforeEach() {

        filledManager = new FileBackedTasksManager("tests.csv");
        emptyManager = new FileBackedTasksManager("tests.csv");

        task = new Task("name", "description");
        epic = new Epic("epicName", "epicDescription");
        subTask = new SubTask("subTaskName", "subTaskDescription", epic);

        filledManager.createEpic(epic);
        filledManager.createSubTask(subTask, epic);
        filledManager.createTask(task);
    }

    @Test
    void saveWhileEmptyTasksList() throws IOException {
        emptyManager.save();
        //При пустом списке задач все-равно создается строка с заглавием + пустая строка, поэтому разммер 2
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).size(), 2);
    }

    @Test
    void saveContainsEpicWithNoSubTasks() throws IOException {
        emptyManager.createEpic(epic);
        emptyManager.save();
        String string = Files.readAllLines(Paths.get("tests.csv")).get(1);
        assertEquals(string, "1,EPIC,epicName,NEW,epicDescription");
    }

    @Test
    void saveContainsFilledHistory() throws IOException {
        //Оба теста выше содержат пустой список просмотров, поэтому третьим тестом я сделаю сохранение с заполненным
        // списком просмотров
        emptyManager.createEpic(epic);
        emptyManager.getEpicByUin(1);
        emptyManager.save();
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(3), "1");
    }
}