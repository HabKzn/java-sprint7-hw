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
        assertEquals(emptyManager.history().size(), 0);
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
        Epic epic = new Epic("epicName", "epicDescription");
        epic.setUin(1);
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);
        subTask.setUin(2);

        assertEquals(filledManager.getEpicByUin(1).toString(), epic.toString());
        assertEquals(filledManager.history().get(0).getId(), 1);
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
        Epic epic = new Epic("epicname", "epicdescription");
        epic.setStatus(Status.DONE);
        epic.setUin(1);
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
    }

    @Test
    void updateSubtaskIfListNotEmpty() {
        assertEquals(filledManager.getTaskUniversal(1).getStatus(), Status.NEW);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        SubTask subtask = new SubTask("name", "Surname", new Epic("name", "Surname"));
        subtask.setStatus(Status.DONE);
        subtask.setUin(2);
        filledManager.updateSubTask(subtask);
        assertEquals(filledManager.getTaskUniversal(2).getStatus(), Status.DONE);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
    }

    @Test
    void updateSubtaskIfListIsEmpty() {
        SubTask subtask = new SubTask("name", "Surname", new Epic("name", "Surname"));
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
        task.setUin(3);
        filledManager.updateTask(task);
        assertEquals(filledManager.getTaskUniversal(3).getStatus(), Status.DONE);
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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteEpicById(1);

        assertEquals(filledManager.getAllEpicsList().size(), 0);
        assertEquals(filledManager.getAllSubTasksList().size(), 0);
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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteEpicById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void deleteSubTaskByIdWhileListExists() {
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteSubTaskById((2));

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 0);
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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteSubTaskById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }


    @Test
    void deleteTaskByIdWhileListExists() {
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteTaskById(3);

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
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
        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);

        filledManager.deleteTaskById(100500);

        assertEquals(filledManager.getAllEpicsList().size(), 1);
        assertEquals(filledManager.getAllSubTasksList().size(), 1);
        assertEquals(filledManager.getAllTasksList().size(), 1);
    }

    @Test
    void historyTestWhileHistoryListIsEmpty() {
        assertEquals(filledManager.history().size(), 0);
    }

    @Test
    void historyTestWhileHistoryListIsNotEmpty() {
        filledManager.getSubTaskByUin(2);
        filledManager.getTaskByUin(3);
        assertEquals(filledManager.history().size(), 2);
    }

    @Test
    void removeFromHistoryByIDWhileHistoryListIsEmpty() {
        filledManager.removeFromHistoryByID(2);
        assertEquals(filledManager.history().size(), 0);
    }

    @Test
    void removeFromHistoryByIDWhileHistoryListIsNotEmpty() {
        filledManager.getSubTaskByUin(2);
        filledManager.getTaskByUin(3);
        filledManager.removeFromHistoryByID(2);
        assertEquals(filledManager.history().size(), 1);
    }

    @Test
    void removeFromHistoryByIDWhileIdIsWrong() {
        filledManager.getSubTaskByUin(2);
        filledManager.getTaskByUin(3);
        filledManager.removeFromHistoryByID(100500);
        assertEquals(filledManager.history().size(), 2);
    }

    @Test
    void getTaskUniversalIfTaskListsNotEmpty() {
        Task task = new Task("name", "description");
        task.setUin(3);
        Epic epic = new Epic("epicName", "epicDescription");
        epic.setUin(1);
        SubTask subTask = new SubTask("subTaskName", "subTaskDescription", epic);
        subTask.setUin(2);

        assertEquals(task.toString(), filledManager.getTaskUniversal(3).toString());
        assertEquals(subTask.toString(), filledManager.getTaskUniversal(2).toString());
        assertEquals(epic.toString(), filledManager.getTaskUniversal(1).toString());

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





