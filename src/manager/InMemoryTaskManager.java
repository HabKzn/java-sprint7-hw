package manager;

import history.HistoryManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int uin;
    public final HashMap<Integer, Task> tasks;
    public final HashMap<Integer, Epic> epics;
    public final HashMap<Integer, SubTask> subTasks;
    public final HistoryManager memoryManager;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        uin = 0;
        memoryManager = Managers.getDefaultHistory();

    }

    public int getUin() {
        return uin;
    }

    public void setUin(final int uin) {
        this.uin = uin;
    }

    //получение списка всех задач по типу
    @Override
    public ArrayList<Epic> getAllEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksList() {
        return new ArrayList<>(subTasks.values());
    }

    // получение списков имен задач

    public ArrayList<String> getTasksNames() {
        ArrayList<String> tasksNames = new ArrayList<>();
        for (Task task : getAllTasksList()) {
            tasksNames.add(task.getName());
        }
        return tasksNames;
    }


    public ArrayList<String> getSubTasksNames() {
        ArrayList<String> tasksNames = new ArrayList<>();
        for (SubTask subTask : getAllSubTasksList()) {
            tasksNames.add(subTask.getName());
        }
        return tasksNames;
    }

    public ArrayList<String> getEpicsNames() {
        ArrayList<String> tasksNames = new ArrayList<>();
        for (Epic epic : getAllEpicsList()) {
            tasksNames.add(epic.getName());
        }
        return tasksNames;
    }

    //удаление всех задач
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        clearSubTasks();
    }

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        epics.clear();
    }


    //получение по идентификатору
    @Override
    public Task getTaskByUin(int uin) {
        if (tasks.get(uin) != null) {
            memoryManager.add(tasks.get(uin));
            return tasks.get(uin);
        } else
            return null;
    }

    @Override
    public Task getSubTaskByUin(int uin) {
        if (subTasks.get(uin) != null) {
            memoryManager.add(subTasks.get(uin));
            return tasks.get(uin);
        } else
            return null;
    }


    public Task getEpicByUin(int uin) {
        if (epics.get(uin) != null) {
            memoryManager.add(epics.get(uin));
            return tasks.get(uin);
        } else
            return null;
    }

    public void createTask(Task task) {
        uin += 1;
        tasks.put(uin, task);
        task.setUin(uin);
        task.setStatus(Status.NEW);
    }

    public void createSubTask(SubTask subtask, Epic epic) {
        uin += 1;
        subTasks.put(uin, subtask);
        subtask.setUin(uin);
        subtask.setStatus(subtask.getStatus());
        epic.addSubTaskToEpicList(subtask);
        epic.setStatus(calculateEpicStatus(epic));

    }

    public void createEpic(Epic epic) {
        uin += 1;
        epics.put(uin, epic);
        epic.setUin(uin);
        epic.setStatus(Status.NEW);
    }

    //Обновление задач.
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }


    public void updateSubTask(SubTask subtask) {
        subTasks.put(subtask.getId(), subtask);
        subtask.getEpic().setStatus(calculateEpicStatus(subtask.getEpic()));
    }


    //Методы для удаления по ID(Вместе с эпиком удаляются и подзадачи)
    @Override
    public void deleteEpicById(int id) {
        for (SubTask subtask : epics.get(id).getSubTasks()) {
            subTasks.remove(subtask.getId());
            removeFromHistoryByID(subtask.getId());
        }
        epics.remove(id);
        removeFromHistoryByID(id);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        removeFromHistoryByID(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
        removeFromHistoryByID(id);
    }


    public List<SubTask> getSubtasks(Epic epic) {
        return epic.getSubTasks();
    }

    @Override
    public List<Task> history() {
        return memoryManager.getTasks();
    }



    public Status calculateEpicStatus(Epic epic) {
        int doneCounter = 0;
        int newCounter = 0;

        if (epic.getSubTasks().isEmpty()) {
            return Status.NEW;
        }

        for (SubTask task : epic.getSubTasks()) {
            if (task.getStatus().equals(Status.DONE)) {
                doneCounter++;
            }else if(task.getStatus().equals(Status.NEW)){
                newCounter++;
            }
        }
        if (doneCounter == epic.getSubTasks().size()) {
            return Status.DONE;
        } else if(newCounter == epic.getSubTasks().size()){
            return Status.NEW;
        }else return Status.IN_PROGRESS;
    }

    //методы для вывода в консоль статусов задач
    public void printEpicsStatus() {
        for (Epic epic : getAllEpicsList()) {
            System.out.println(epic.getDescription() + " со статусом:           " + epic.getStatus());
        }
    }

    public void printSubclassesStatus() {
        for (SubTask subTask : getAllSubTasksList()) {
            System.out.println(subTask.getDescription() + " со статусом:           " + subTask.getStatus());
        }
    }

    public void printTasksStatus() {
        for (Task task : getAllTasksList()) {
            System.out.println(task.getDescription() + " со статусом:           " + task.getStatus());
        }
    }

    @Override
    public void printHistory() {
        if (history().size() == 0) {
            System.out.println("Просмотренных задач нет");
        } else {
            for (int i = history().size() - 1; i >= 0; i--) {
                System.out.println(history().get(i).getName());
            }
        }
    }

    @Override
    public void removeFromHistoryByID(final int id) {
        memoryManager.remove(id);
    }


   public Task getTaskUniversal(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else return subTasks.getOrDefault(id, null);
    }

}


