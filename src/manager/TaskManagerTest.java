package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T emptyManager;
    T filledManager;


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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
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
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
    }

    @Test
    public void getAllSubTasksListWhileSubTasksDontExist() {
        assertEquals(emptyManager.getAllSubTasksList().size(), 0);
    }

    @Test
    public void getTaskByUinWithEmptyTasksList() {
        assertNull(emptyManager.getTaskByUin(3));
        assertNull(emptyManager.history());
    }

    @Test
    public void getTaskByUinWithFilledTasksList() {
        Task task = new Task("name", "description");
        task.setUin(3);
        assertEquals(filledManager.getTaskByUin(3), task);
        assertEquals(filledManager.history().get(0).getId(), 3);
    }

    @Test
    public void getTaskByUinWithIncorrectUin() {
        assertNull(filledManager.getTaskByUin(100500));
        assertNull(filledManager.history());
    }

    @Test
    public void getEpicByUinWithEmptyEpicsList() {
        assertNull(emptyManager.getEpicByUin(3));
        assertNull(emptyManager.history());
    }

    @Test
    public void getEpicByUinWithFilledTasksList() {
        Epic epic = new Epic("epicName", "epicDescription");
        epic.setUin(1);
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);
        subTask.setUin(2);

        assertEquals(filledManager.getEpicByUin(1).toString(), epic.toString());
        assertEquals(filledManager.history().get(0).getId(), 1);
    }

    @Test
    public void getEpicByUinWithIncorrectUin() {
        assertNull(filledManager.getTaskByUin(100500));
        assertNull(filledManager.history());
    }

    @Test
    public void getSubTaskByUinWithEmptyTasksList() {
        assertNull(emptyManager.getSubTaskByUin(2));
        assertNull(emptyManager.history());
    }

    @Test
    public void getSubTaskByUinWithFilledTasksList() {
        Epic epic = new Epic("epicName", "epicDescription");
        epic.setUin(1);
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);
        subTask.setUin(2);

        assertEquals(filledManager.getSubTaskByUin(2).toString(), subTask.toString());
        assertEquals(filledManager.history().get(0).getId(), 2);
    }

    @Test
    public void getSubTaskByUinWithIncorrectUin() {
        assertNull(filledManager.getSubTaskByUin(100500));
        assertNull(filledManager.history());
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
        Epic epic = new Epic("epicname", "epicdescription");
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription", epic);
        assertEquals(1, filledManager.getAllSubTasksList().size());
        filledManager.createSubTask(subtask, epic);
        assertEquals(2, filledManager.getAllSubTasksList().size());
    }

    @Test
    void createSubTaskWithEmptyTaskList() {
        Epic epic = new Epic("epicname", "epicdescription");
        SubTask subtask = new SubTask("subtaskname", "subtaskdescription", epic);
        assertEquals(0, emptyManager.getAllSubTasksList().size());
        emptyManager.createSubTask(subtask, epic);
        assertEquals(1, emptyManager.getAllSubTasksList().size());
    }

    @Test
    void createEpicWithNotEmptyEpicList() {
        Epic epic = new Epic("epicname", "epicdescription");
        assertEquals(1, filledManager.getAllEpicsList().size());
        filledManager.createEpic(epic);
        assertEquals(2, filledManager.getAllEpicsList().size());
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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        Epic epic = filledManager.getAllEpicsList().get(0);
        epic.setStatus(Status.DONE);
        filledManager.updateEpic(epic);
        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.DONE);
        assertEquals(filledManager.getAllEpicsList().size(), 1);
    }

    @Test
    void updateEpicIfEpicListIsEmpty() {
        Epic epic = new Epic("name", "Surname");
        epic.setStatus(Status.DONE);
        emptyManager.updateEpic(epic);
        assertNull(emptyManager.getTaskUniversal(epic.getId()));
        assertEquals(emptyManager.getAllEpicsList().size(), 0);
    } @Test
//
//    void updateSubtaskIfListNotEmpty() {
//        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.NEW);
//        assertEquals(filledManager.getAllEpicsList().size(), 1);
//        Epic epic = filledManager.getAllEpicsList().get(0);
//        epic.setStatus(Status.DONE);
//        filledManager.updateEpic(epic);
//        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.DONE);
//        assertEquals(filledManager.getAllEpicsList().size(), 1);
//    }
//
//    @Test
//    void updateSubtaskIfListIsEmpty() {
//        Epic epic = new Epic("name", "Surname");
//        epic.setStatus(Status.DONE);
//        emptyManager.updateEpic(epic);
//        assertNull(emptyManager.getTaskUniversal(epic.getId()));
//        assertEquals(emptyManager.getAllEpicsList().size(), 0);
//    }
//
//    @Test
//    void updateTaskIfListNotEmpty() {
//        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.NEW);
//        assertEquals(filledManager.getAllEpicsList().size(), 1);
//        Epic epic = filledManager.getAllEpicsList().get(0);
//        epic.setStatus(Status.DONE);
//        filledManager.updateEpic(epic);
//        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.DONE);
//        assertEquals(filledManager.getAllEpicsList().size(), 1);
//    }
//
//    @Test
//    void updateTaskIfListIsEmpty() {
//        Epic epic = new Epic("name", "Surname");
//        epic.setStatus(Status.DONE);
//        emptyManager.updateEpic(epic);
//        assertNull(emptyManager.getTaskUniversal(epic.getId()));
//        assertEquals(emptyManager.getAllEpicsList().size(), 0);
//    }



}