package tests;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @BeforeEach
    void createTasks() {
        Epic epc1 = new Epic("epc1", "this is epic N1");
        Epic epc2 = new Epic("epc2", "this is epic N2");
        Epic epc3 = new Epic("epc3", "this is epic3");
        Epic epc4 = new Epic("epc4", "this is epic4");
        Epic epc5 = new Epic("epc5", "this is epic5");

        SubTask sbt1 = new SubTask("sbt1", "this is subtask1", epc1.getId(), LocalDateTime.of(2023, 4, 17, 11, 0), Duration.ofMinutes(60));
        SubTask sbt2 = new SubTask("sbt2", "this is subtask2", epc1.getId(), LocalDateTime.of(2023, 4, 17, 13, 0), Duration.ofMinutes(60));
        SubTask sbt3 = new SubTask("sbt3", "this is subtask3", epc2.getId(), LocalDateTime.of(2023, 4, 17, 15, 0), Duration.ofMinutes(60));
        SubTask sbt4 = new SubTask("sbt4", "this is subtask4", epc2.getId(), LocalDateTime.of(2023, 4, 17, 17, 0), Duration.ofMinutes(60));
        SubTask sbt5 = new SubTask("sbt5", "this is subtask5", epc4.getId(), LocalDateTime.of(2023, 4, 17, 19, 0), Duration.ofMinutes(60));
        SubTask sbt6 = new SubTask("sbt6", "this is subtask6", epc4.getId(), LocalDateTime.of(2023, 4, 17, 21, 0), Duration.ofMinutes(60));
        SubTask sbt7 = new SubTask("sbt7", "this is subtask7", epc5.getId(), LocalDateTime.of(2023, 4, 17, 23, 0), Duration.ofMinutes(60));
        SubTask sbt8 = new SubTask("sbt8", "this is subtask8", epc5.getId(), LocalDateTime.of(2023, 4, 17, 9, 0), Duration.ofMinutes(60));

        manager.createEpic(epc1); // id = 1
        manager.createEpic(epc2); // id = 2
        manager.createEpic(epc3); // id = 3
        manager.createSubTask(sbt1, epc1.getId()); // id = 4
        manager.createSubTask(sbt2, epc1.getId()); // id = 5
        sbt3.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(sbt3);
        sbt4.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(sbt4);
        manager.createSubTask(sbt3, epc2.getId()); // id = 6
        manager.createSubTask(sbt4, epc2.getId()); // id = 7
        manager.createEpic(epc4); // id = 8
        sbt5.setStatus(Status.DONE);
        sbt6.setStatus(Status.DONE);
        manager.updateSubTask(sbt5);
        manager.updateSubTask(sbt6);
        manager.createSubTask(sbt5, epc4.getId()); //id = 9
        manager.createSubTask(sbt6, epc4.getId());// id = 10
        manager.createEpic(epc5); // id = 11
        sbt8.setStatus(Status.DONE);
        manager.updateSubTask(sbt8);
        manager.createSubTask(sbt7, epc5.getId());// id = 12
        manager.createSubTask(sbt8, epc5.getId()); // id = 13
    }


    @Test
    void calculateEpicStatusIfNoSubtasks() {
        Task epic = manager.getTaskUniversal(3);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void calculateEpicStatusIfAllSubtasksNew() {
        Task epic = manager.getTaskUniversal(1);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void calculateEpicStatusIfAllSubtasksDone() {
        Task epic = manager.getTaskUniversal(8);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void calculateEpicStatusIfSubtasksNewAndDone() {
        Task epic = manager.getTaskUniversal(11);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void calculateEpicStatusIfSubTasksInProgress() {
        assertEquals(manager.getTaskUniversal(2).getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void getSubTasksWhileSubTasksListFilled() {
        Epic epic = (Epic) manager.getEpicByUin(1);
        assertEquals(epic.getSubtasks().size(), 2);
    }

    @Test
    void getSubTasksWhileSubTasksListIsEmpty() {
        Epic epic = (Epic) manager.getEpicByUin(3);
        assertEquals(epic.getSubtasks().size(), 0);
    }
}