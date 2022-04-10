package tasks;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EpicTest {
    static Epic epc1 = new Epic("epc1", "this is epic N1");
    static Epic epc2 = new Epic("epc2", "this is epic N2");
    static Epic epc3 = new Epic("epc3", "this is epic3");
    static Epic epc4 = new Epic("epc4", "this is epic4");
    static Epic epc5 = new Epic("epc5", "this is epic5");

    static SubTask sbt1 = new SubTask("sbt1", "this is subtask1", epc1);
    static SubTask sbt2 = new SubTask("sbt2", "this is subtask2", epc1);
    static SubTask sbt3 = new SubTask("sbt3", "this is subtask3", Status.DONE, epc2);
    static SubTask sbt4 = new SubTask("sbt4", "this is subtask4", Status.DONE, epc2);
    static SubTask sbt5 = new SubTask("sbt5", "this is subtask5", Status.IN_PROGRESS, epc4);
    static SubTask sbt6 = new SubTask("sbt6", "this is subtask6", Status.IN_PROGRESS, epc4);
    static SubTask sbt7 = new SubTask("sbt7", "this is subtask7", Status.NEW, epc5);
    static SubTask sbt8 = new SubTask("sbt8", "this is subtask8", Status.DONE, epc5);


    static InMemoryTaskManager manager = new InMemoryTaskManager();

    @BeforeAll
    static void createTasks() {
        manager.createEpic(epc1); // id = 1
        manager.createEpic(epc2); // id = 2
        manager.createEpic(epc3); // id = 3
        manager.createSubTask(sbt1, epc1); // id = 4
        manager.createSubTask(sbt2, epc1); // id = 5
        manager.createSubTask(sbt3, epc2); // id = 6
        manager.createSubTask(sbt4, epc2); // id = 7
        manager.createEpic(epc4); // id = 8
        manager.createSubTask(sbt5, epc4); //id = 9
        manager.createSubTask(sbt6, epc4);// id = 10
        manager.createEpic(epc5); // id = 11
        manager.createSubTask(sbt7, epc5);// id = 12
        manager.createSubTask(sbt8, epc5); // id = 13
    }


    @Test
    void calculateEpicStatusIfNoSubtasks() {

        Task epic = manager.getTaskUniversal(3);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void calculateEpicStatusIfAllSubtasksNew() {
        Task epic = manager.getTaskUniversal(1);
        Assertions.assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void calculateEpicStatusIfAllSubtasksDone() {
        Task epic = manager.getTaskUniversal(2);
        Assertions.assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void calculateEpicStatusIfSubtasksNewAndDone() {
        Task epic = manager.getTaskUniversal(11);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void calculateEpicStatusIfSubTasksInProgress() {
        Task epic = manager.getTaskUniversal(8);
        Assertions.assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}