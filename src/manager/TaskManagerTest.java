package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T emptyManager;
    T filledManager;
    Task task = new Task("name", "description",
            LocalDateTime.of(2022, 4, 17, 10, 0), Duration.ofMinutes(50));

    Epic epic1 = new Epic("epic1Name", "epic1Description");

    SubTask subTask11 = new SubTask("subTask11Name", "subTask11Description", epic1,
            LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));

    SubTask subTask12 = new SubTask("subTask12Name", "subTask12Description", epic1,
            LocalDateTime.of(2022, 4, 16, 11, 0), Duration.ofMinutes(60));

    Epic epic2 = new Epic("epic2Name", "epic2Description");

    SubTask subTask21 = new SubTask("subTask21Name", "subTask21Description", epic2,
            LocalDateTime.of(2022, 4, 16, 13, 0), Duration.ofMinutes(60));


    String taskTestString = "1,TASK,name,NEW,description,2022-04-17T10:00,PT50M,2022-04-17T10:50";
    String epic1TestString = "2,EPIC,epic1Name,NEW,epic1Description,2022-04-16T10:00,PT2H,2022-04-16T12:00";
    String subTask11TestString = "3,SUBTASK,subTask11Name,NEW,subTask11Description,2,2022-04-16T10:00,PT1H,2022-04-16T11:00";
    String subTask12TestString = "4,SUBTASK,subTask12Name,NEW,subTask12Description,2,2022-04-16T11:00,PT1H,2022-04-16T12:00";
    String epic2TestString = "5,EPIC,epic2Name,DONE,epic2Description,2022-04-16T13:00,PT2H,2022-04-16T14:00";
    String subTask21TestString = "6,SUBTASK,subTask21Name,DONE,subTask21Description,5,2022-04-16T13:00,PT1H,2022-04-16T14:00";


    @Test
    void clearTasksWhileTasksArePresent() {
        filledManager.clearTasks();
        assertEquals(filledManager.getAllTasksList().size(), 0);
    }

    @Test
    void clearTasksWhileTaskListDontExist() {
        emptyManager.clearTasks();
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    void clearSubTasksWhileSubTasksArePresent() {
        filledManager.clearSubTasks();
        assertEquals(filledManager.getAllSubTasksList().size(), 0);
    }

    @Test
    void clearSubTasksWhileSubTaskListDontExist() {
        emptyManager.clearSubTasks();
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
    }

    @Test
    void clearEpicsWhileSubTasksArePresent() {
        filledManager.clearEpics();
        assertEquals(filledManager.getAllEpicsList().size(), 0);
    }

    @Test
    void clearEpicsWhileSubTaskListDontExist() {
        emptyManager.clearEpics();
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
    }

    @Test
    public void getAllEpicsListWhileEpicsArePresent() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
    }

    @Test
    public void getAllEpicsListWhileEpicsDontExist() {
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
    }

    @Test
    public void getAllTasksListWhileTasksArePresent() {
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    public void getAllTasksListWhileTasksDontExist() {
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    public void getAllSubTasksListWhileSubTasksArePresent() {
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
    }

    @Test
    public void getAllSubTasksListWhileSubTasksDontExist() {
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
    }

    @Test
    public void getTaskByUinWithEmptyTasksList() {
        assertNull(emptyManager.getTaskByUin(3));
        assertEquals(emptyManager.history().size(), 0);
    }

    @Test
    public void getTaskByUinWithFilledTasksList() {
        assertEquals(filledManager.getTaskByUin(1).toString(), taskTestString);
        assertEquals(filledManager.history().get(0).getId(), 1);
    }

    @Test
    public void getTaskByUinWithIncorrectUin() {
        int i = filledManager.history().size();
        assertNull(filledManager.getTaskByUin(100500));
        assertEquals(i, filledManager.history().size()); //история не меняется
    }

    @Test
    public void getEpicByUinWithEmptyEpicsList() {
        assertNull(emptyManager.getEpicByUin(3));
        assertEquals(emptyManager.history().size(), 0);
    }

    @Test
    public void getEpicByUinWithFilledTasksList() {

    }

    @Test
    public void getEpicByUinWithIncorrectUin() {
        int i = filledManager.history().size();
        assertNull(filledManager.getTaskByUin(100500));
        assertEquals(filledManager.history().size(), i);
    }

    @Test
    public void getSubTaskByUinWithEmptyTasksList() {
        assertNull(emptyManager.getSubTaskByUin(2));
        assertEquals(emptyManager.history().size(), 0);
    }

    @Test
    public void getSubTaskByUinWithFilledTasksList() {
        assertEquals(filledManager.getSubTaskByUin(3).toString(), subTask11TestString);
        assertEquals(filledManager.history().get(0).getId(), 3);
    }

    @Test
    public void getSubTaskByUinWithIncorrectUin() {
        assertNull(filledManager.getSubTaskByUin(100500));
        assertEquals(filledManager.history().size(), 0);
    }

    @Test
    void createTaskWithNotEmptyTaskList() {
        Task task = new Task("newTaskName", "NewTaskDescription");
        assertEquals(1, filledManager.getAllTasksList().size());
        filledManager.createTask(task);
        assertEquals(2, filledManager.getAllTasksList().size());
    }

    @Test
    void createTaskWithEmptyTaskList() {
        Task task = new Task("newTaskName", "NewTaskDescription");
        assertEquals(0, emptyManager.getAllTasksList().size());
        emptyManager.createTask(task);
        assertEquals(1, emptyManager.getAllTasksList().size());
    }

    @Test
    void createSubTaskWithNotEmptyTaskList() {
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription",
                 (Epic) filledManager.getTaskUniversal(2),
                 LocalDateTime.of(2022, 4,17, 11, 0),
                 Duration.ofMinutes(40));
        assertEquals(3, filledManager.getAllSubTasksList().size());
        filledManager.createSubTask(subtask, subtask.getEpic());
        assertEquals(4, filledManager.getAllSubTasksList().size());
    }

    @Test
    void createSubTaskWithEmptyTaskList() {
        Epic epic = new Epic("epicname", "epicdescription");
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription", epic,
                LocalDateTime.of(2022, 4,17, 11, 0), Duration.ofMinutes(40));
        assertEquals(0, emptyManager.getAllSubTasksList().size());
        emptyManager.createSubTask(subtask, epic);
        assertEquals(1, emptyManager.getAllSubTasksList().size());
    }

    @Test
    void createEpicWithNotEmptyEpicList() {
        assertEquals(2, filledManager.getAllEpicsList().size());
        filledManager.createEpic(new Epic("name", "description"));
        assertEquals(3, filledManager.getAllEpicsList().size());
    }

    @Test
    void createEpicWithEmptyEpicList() {
        Epic epic = new Epic("epicname", "epicdescription");
        assertEquals(0, emptyManager.getAllEpicsList().size());
        emptyManager.createEpic(epic);
        assertEquals(1, emptyManager.getAllEpicsList().size());
    }

    @Test
    void updateEpicIfEpicListNotEmpty() {
        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.NEW);
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        epic1.setStatus(Status.DONE);
        filledManager.updateEpic(epic1);
        assertEquals(filledManager.getTaskUniversal(2).getStatus(), Status.DONE);
        assertEquals(filledManager.getAllEpicsList().size(), 2);
    }

    @Test
    void updateEpicIfEpicListIsEmpty() {
        Epic epic = new Epic("name", "Surname");
        epic.setStatus(Status.DONE);
        emptyManager.updateEpic(epic);
        assertNull(emptyManager.getTaskUniversal(epic.getId()));
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
    }

    @Test
    void updateSubtaskIfListNotEmpty() {
        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.NEW);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        SubTask sbt = (SubTask) filledManager.getTaskUniversal(3);
        sbt.setStatus(Status.DONE);
        filledManager.updateSubTask(sbt);
        assertEquals(filledManager.getTaskUniversal(3).getStatus(), Status.DONE);
       assertEquals(filledManager.getTaskUniversal(2).getStatus(), Status.IN_PROGRESS);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
    }

    @Test
    void updateSubtaskIfListIsEmpty() {
        SubTask subtask = new SubTask("name", "Surname", new Epic("name", "Surname"),
                LocalDateTime.of(2022, 4,17, 11, 0), Duration.ofMinutes(40));
        subtask.setStatus(Status.DONE);
        emptyManager.updateSubTask(subtask);
        assertNull(emptyManager.getTaskUniversal(subtask.getId()));
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
    }

    @Test
    void updateTaskIfListNotEmpty() {
        assertEquals(filledManager.getTaskUniversal(3).getStatus(), Status.NEW);
        assertEquals(filledManager.getAllTasksList().size(), 1);
        Task task = new Task("a", "b");
        task.setStatus(Status.DONE);
        task.setUin(1);
        filledManager.updateTask(task);
        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.DONE);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void updateTaskIfListIsEmpty() {
        Task task = new Epic("name", "Surname");
        task.setStatus(Status.DONE);
        emptyManager.updateTask(task);
        assertNull(emptyManager.getTaskUniversal(3));
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    void deleteEpicByIdWhileListExists() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteEpicById(2);

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void deleteEpicByIdWhileListIsEmpty() {
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);

        emptyManager.deleteEpicById(1);

        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    void deleteEpicByIdWhileIdIsWrong() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteEpicById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void deleteSubTaskByIdWhileListExists() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteSubTaskById((3));

        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 2);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void deleteSubTaskByIdWhileListIsEmpty() {
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);

        emptyManager.deleteSubTaskById(1);

        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    void deleteSubTaskByIdWhileIdIsWrong() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteSubTaskById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }


    @Test
    void deleteTaskByIdWhileListExists() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteTaskById(1);

        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 0);
    }

    @Test
    void deleteTaskByIdWhileListIsEmpty() {
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);

        emptyManager.deleteTaskById(3);

        assertEquals(emptyManager.getAllEpicsList().size(), 0);
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    void deleteTaskByIdWhileIdIsWrong() {
        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteTaskById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 2);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void historyTestWhileHistoryListIsEmpty() {
        assertEquals(filledManager.history().size(), 0);
    }

    @Test
    void historyTestWhileHistoryListIsNotEmpty() {
        filledManager.getSubTaskByUin(3);
        filledManager.getTaskByUin(1);
        assertEquals(filledManager.history().size(), 2);
    }

    @Test
    void removeFromHistoryByIDWhileHistoryListIsEmpty() {
        filledManager.removeFromHistoryByID(2);
        assertEquals(filledManager.history().size(), 0);
    }

    @Test
    void removeFromHistoryByIDWhileHistoryListIsNotEmpty() {
        filledManager.getSubTaskByUin(3);
        filledManager.getTaskByUin(1);
        filledManager.removeFromHistoryByID(3);
        assertEquals(filledManager.history().size(), 1);
    }

    @Test
    void removeFromHistoryByIDWhileIdIsWrong() {
        filledManager.getSubTaskByUin(3);
        filledManager.getTaskByUin(1);
        filledManager.removeFromHistoryByID(100500);
        assertEquals(filledManager.history().size(), 2);
    }

    @Test
    void getTaskUniversalIfTaskListsNotEmpty() {
        System.out.println(filledManager.getTaskUniversal(4).toString());
        System.out.println(filledManager.getTaskUniversal(3).toString());
        System.out.println(filledManager.getTaskUniversal(2).toString());
        System.out.println(filledManager.getTaskUniversal(1).toString());
        System.out.println(1);
    }

    @Test
    void getTaskUniversalIfTaskListsAreEmpty() {
        assertNull(emptyManager.getTaskUniversal(1));
    }

    @Test
    void getTaskUniversalIfWrongId() {
        assertNull(filledManager.getTaskUniversal(100500));
    }

}





