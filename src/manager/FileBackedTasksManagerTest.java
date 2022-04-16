package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {


    @BeforeEach
    public void beforeEach() {
        filledManager = new FileBackedTasksManager("tests.csv");
        emptyManager = new FileBackedTasksManager("tests.csv");

        filledManager.createTask(task);
        filledManager.createEpic(epic1);
        filledManager.createSubTask(subTask11, epic1);
        filledManager.createSubTask(subTask12, epic1);
        filledManager.createEpic(epic2);
        filledManager.createSubTask(subTask21, epic2);
        subTask21.setStatus(Status.DONE);
        filledManager.updateSubTask(subTask21);
    }

    @Test
    void saveWhileEmptyTasksList() throws IOException {
        emptyManager.save();
        //При пустом списке задач все-равно создается строка с заглавием + пустая строка, поэтому размер 2
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).size(), 2);
    }

    @Test
    void saveContainsEpicWithNoSubTasks() throws IOException {
        emptyManager.createEpic(new Epic("epicName", "epicDescription"));
        emptyManager.save();
        String string = Files.readAllLines(Paths.get("tests.csv")).get(1);
        assertEquals(string, "1,EPIC,epicName,NEW,epicDescription,null,PT0S,null");
    }

    @Test
    void saveContainsFilledHistory() throws IOException {
        emptyManager.createEpic(epic1);
        emptyManager.getEpicByUin(1);
        emptyManager.save();
        Assertions.assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(3), "1");
    }

    @Test
    void justSaveFilledManager() throws IOException {
        filledManager.getTaskByUin(1);
        filledManager.getSubTaskByUin(3);
        filledManager.getEpicByUin(2);
        filledManager.save();
        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(1),
                "1,TASK,name,NEW,description,2022-04-17T10:00,PT50M,2022-04-17T10:50");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(2),
                "3,SUBTASK,subTask11Name,NEW,subTask11Description,2,2022-04-16T10:00,PT1H,2022-04-16T11:00");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(3),
                "4,SUBTASK,subTask12Name,NEW,subTask12Description,2,2022-04-16T11:00,PT1H,2022-04-16T12:00");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(4),
                "6,SUBTASK,subTask21Name,DONE,subTask21Description,5,2022-04-16T13:00,PT1H,2022-04-16T14:00");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(5),
                "2,EPIC,epic1Name,NEW,epic1Description,2022-04-16T10:00,PT2H,2022-04-16T12:00");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(6),
                "5,EPIC,epic2Name,DONE,epic2Description,2022-04-16T13:00,PT1H,2022-04-16T14:00");

        assertEquals(Files.readAllLines(Paths.get("tests.csv")).get(8), "1,3,2");

    }
}