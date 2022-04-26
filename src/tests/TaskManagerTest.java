package tests;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
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
    Task task;
    Epic epic1;
    SubTask subTask11;
    SubTask subTask12;
    Epic epic2;
    SubTask subTask21;

    String taskTestString = "1,TASK,name,NEW,description,2022-04-17T09:00,PT50M,2022-04-17T09:50";
    String epic1TestString = "2,EPIC,epic1Name,NEW,epic1Description,2022-04-16T10:00,PT2H,2022-04-16T12:00";
    String subTask11TestString = "3,SUBTASK,subTask11Name,NEW,subTask11Description,2,2022-04-16T10:00,PT1H,2022-04-16T11:00";
    String subTask12TestString = "4,SUBTASK,subTask12Name,NEW,subTask12Description,2,2022-04-16T11:00,PT1H,2022-04-16T12:00";



    @BeforeEach
    void createTasks() {
        task = new Task("name", "description",
                LocalDateTime.of(2022, 4, 17, 9, 0), Duration.ofMinutes(50));

        epic1 = new Epic("epic1Name", "epic1Description");

        subTask11 = new SubTask("subTask11Name", "subTask11Description", epic1.getId(),
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));

        subTask12 = new SubTask("subTask12Name", "subTask12Description", epic1.getId(),
                LocalDateTime.of(2022, 4, 16, 11, 0), Duration.ofMinutes(60));

        epic2 = new Epic("epic2Name", "epic2Description");

        subTask21 = new SubTask("subTask21Name", "subTask21Description", epic2.getId(),
                LocalDateTime.of(2022, 4, 16, 13, 0), Duration.ofMinutes(60));
    }


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
        assertEquals(filledManager.getAllEpicsList().get(0).getId(), 2);
        assertEquals(filledManager.getAllEpicsList().get(1).getId(), 5);
    }

    @Test
    public void getAllEpicsListWhileEpicsDontExist() {
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
    }

    @Test
    public void getAllTasksListWhileTasksArePresent() {
        assertEquals(filledManager.getAllTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().get(0).getId(), 1);
    }

    @Test
    public void getAllTasksListWhileTasksDontExist() {
        assertEquals(emptyManager.getAllTasksList().size(), 0);
    }

    @Test
    public void getAllSubTasksListWhileSubTasksArePresent() {
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
        assertEquals(filledManager.getAllSubTasksList().get(0).getId(), 3);
        assertEquals(filledManager.getAllSubTasksList().get(1).getId(), 4);
        assertEquals(filledManager.getAllSubTasksList().get(2).getId(), 6);
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
        assertEquals(filledManager.getEpicByUin(2).toString(), epic1TestString);
        assertEquals(filledManager.history().size(), 1);
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
        Task task = new Task("newTaskName", "NewTaskDescription",
                LocalDateTime.of(2022, 4, 19, 10, 0), Duration.ofMinutes(60));
        assertEquals(1, filledManager.getAllTasksList().size());
        filledManager.createTask(task);
        assertEquals(2, filledManager.getAllTasksList().size());
    }

    @Test
    void createTaskWithEmptyTaskList() {
        Task task = new Task("newTaskName", "NewTaskDescription",
                LocalDateTime.of(2032, 1, 12, 3, 44), Duration.ofMinutes(60));
        assertEquals(0, emptyManager.getAllTasksList().size());
        emptyManager.createTask(task);
        assertEquals(1, emptyManager.getAllTasksList().size());
    }

    @Test
    void createSubTaskWithNotEmptyTaskList() {
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription",
                2,
                LocalDateTime.of(2022, 4, 17, 11, 0),
                Duration.ofMinutes(40));
        assertEquals(3, filledManager.getAllSubTasksList().size());
        filledManager.createSubTask(subtask, subtask.getEpicId());
        assertEquals(4, filledManager.getAllSubTasksList().size());
    }

    @Test
    void createSubTaskWithEmptyTaskList() {
        Epic epic = new Epic("epicname", "epicdescription");
        epic.setStartTime(LocalDateTime.of(2011,2,3,12,3));
        epic.setDuration(Duration.ofMinutes(12));
       emptyManager.createEpic(epic);
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription", epic.getId(),
                LocalDateTime.of(2022, 4, 17, 11, 0), Duration.ofMinutes(40));
        assertEquals(0, emptyManager.getAllSubTasksList().size());
        emptyManager.createSubTask(subtask, epic.getId());
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
        Epic newEpic = cloneEpic(epic1);
        newEpic.setDescription("newDescription");
        filledManager.updateEpic(newEpic);
        assertEquals(filledManager.getTaskUniversal(2).getDescription(), "newDescription");
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
        SubTask oldSubTask = (SubTask) filledManager.getTaskUniversal(3);
        SubTask sbt = cloneSubTask((SubTask) filledManager.getTaskUniversal(3));
        sbt.setStatus(Status.DONE);
        filledManager.updateSubTask(sbt);
        assertEquals(filledManager.getTaskUniversal(3).getStatus(), Status.DONE);
        assertEquals(filledManager.getTaskUniversal(2).getStatus(), Status.IN_PROGRESS);
        assertEquals(filledManager.getAllSubTasksList().size(), 3);
    }

    @Test
    void updateSubtaskIfListIsEmpty() {
        SubTask subtask = new SubTask("name", "Surname", 1,
                LocalDateTime.of(2022, 4, 17, 11, 0), Duration.ofMinutes(40));
        subtask.setStatus(Status.DONE);
        emptyManager.updateSubTask(subtask);
        assertNull(emptyManager.getTaskUniversal(subtask.getId()));
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
    }

    @Test
    void updateTaskIfListNotEmpty() {
        assertEquals(filledManager.getTaskUniversal(3).getStatus(), Status.NEW);
        assertEquals(filledManager.getAllTasksList().size(), 1);
        Task task = cloneTask(filledManager.getTaskUniversal(1));
        task.setStatus(Status.DONE);
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
        assertEquals(filledManager.getTaskUniversal(4).toString(), subTask12TestString);
        assertEquals(filledManager.getTaskUniversal(3).toString(), subTask11TestString);
        assertEquals(filledManager.getTaskUniversal(2).toString(), epic1TestString);
        assertEquals(filledManager.getTaskUniversal(1).toString(), taskTestString);
    }

    @Test
    void getTaskUniversalIfTaskListsAreEmpty() {
        assertNull(emptyManager.getTaskUniversal(1));
    }

    @Test
    void getTaskUniversalIfWrongId() {
        assertNull(filledManager.getTaskUniversal(100500));
    }

    @Test
    void getPrioritizedMethodTestIfManagerIsEmpty() {
        assertEquals(emptyManager.getPrioritizedTask().size(), 0);
    }

    @Test
    void getPrioritizedMethodTestIfManagerIsNotEmpty() {
        assertEquals(filledManager.getPrioritizedTask().size(), 4);
        assertEquals(filledManager.getPrioritizedTask().get(0).toString(), subTask11TestString);
        assertEquals(filledManager.getPrioritizedTask().get(3).toString(), taskTestString);
    }

    @Test
    void changeStartTimeOfTaskTestToFreeTimed() {
        Task task = cloneTask(filledManager.getTaskUniversal(1));
        task.setStartTime(LocalDateTime.of(2000, 04, 17, 6, 0));
        filledManager.updateTask(task);
        assertEquals("2000-04-17T06:00", filledManager.getTaskUniversal(1).getStartTime().toString());
    }

    @Test
    void changeStartTimeOfTaskTestToOccupiedTime() {
        Task oldTask = cloneTask(filledManager.getTaskUniversal(1));
        Task changedTask = cloneTask(oldTask);
        changedTask.setStartTime(LocalDateTime.of(2022, 04, 16, 10, 30));
        filledManager.updateTask(changedTask);
        assertEquals("2022-04-17T09:00", filledManager.getTaskUniversal(1).getStartTime().toString());
    }

    @Test
    void changeStartTimeOfSubTaskTestToFreeTime() {
        SubTask subTask = cloneSubTask((SubTask) filledManager.getTaskUniversal(3));
        subTask.setStartTime(LocalDateTime.of(2025, 04, 16, 13, 30));
        filledManager.updateSubTask(subTask);
        assertEquals("2025-04-16T13:30", filledManager.getTaskUniversal(3).getStartTime().toString());
        assertEquals("2025-04-16T14:30", filledManager.getTaskUniversal(2).getEndTime().toString());
    }

    @Test
    void changeStartTimeOfSubTaskTestToOccupiedTime() {
        SubTask subTask = cloneSubTask((SubTask) filledManager.getTaskUniversal(3));
        subTask.setStartTime(LocalDateTime.of(2022, 4, 16, 13, 30));
        filledManager.updateTask(subTask);
        assertEquals("2022-04-16T10:00", filledManager.getTaskUniversal(3).getStartTime().toString());
        assertEquals("2022-04-16T10:00", filledManager.getTaskUniversal(2).getStartTime().toString());
    }

    //    служебные методы для некоторых тестов:
    public SubTask cloneSubTask(SubTask subTask) {
        SubTask newSubTask = new SubTask(subTask.getName(), subTask.getDescription(), subTask.getEpicId(), subTask.getStartTime(), subTask.getDuration());
        newSubTask.setUin(subTask.getId());
        newSubTask.setStatus(subTask.getStatus());
        return newSubTask;
    }

    public Task cloneTask(Task taskToClone) {
        Task newTask = new Task(taskToClone.getName(), taskToClone.getDescription(), taskToClone.getStartTime(), taskToClone.getDuration());
        newTask.setUin(taskToClone.getUin());
        newTask.setStatus(taskToClone.getStatus());
        return newTask;
    }

    public Epic cloneEpic(Epic epicToClone) {
        Epic newEpic = new Epic(epicToClone.getName(), epicToClone.getDescription());
        for (SubTask subtask : epicToClone.getSubTasks()) {
            newEpic.addSubtask(subtask);
        }
        newEpic.setUin(epicToClone.getUin());
        newEpic.refreshEpicData();
        newEpic.refreshEndTime();
        return newEpic;
    }
}





