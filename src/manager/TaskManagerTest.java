package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
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

    void createSubTaskWithNotEmptyTaskList() {
        Task task = new Task("newTaskName", "NewTaskDescription");
        assertEquals(1, filledManager.getAllTasksList().size());
        filledManager.createTask(task);
        assertEquals(2, filledManager.getAllTasksList().size());
    }

    void createSubTaskWithEmptyTaskList() {
        Task task = new Task("newTaskName", "NewTaskDescription");
        assertEquals(1, filledManager.getAllTasksList().size());
        filledManager.createTask(task);
        assertEquals(2, filledManager.getAllTasksList().size());
    }

}