package manager;

import ServersAndClient.KVServer;
import ServersAndClient.KvTaskClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private KvTaskClient client;
    private Gson gson;
    public HTTPTaskManager(URI url) {
        client = new KvTaskClient(url);
    }

    @Override
    public void save() {
        gson = new Gson();
        List<Integer> historyList = new ArrayList<>();
        for(Task task:history()){
            historyList.add(task.getId());
        }

        String tasks = gson.toJson(getAllTasksList());
        String subTasks = gson.toJson(getAllSubTasksList());
        String epics = gson.toJson(getAllEpicsList());
        String history = gson.toJson(historyList);

        try {
            client.put("tasksKey", tasks);
            client.put("epicsKey", epics);
            client.put("subTasksKey", subTasks);
            client.put("historyKey", history);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void createTaskWhileLoad(Task task) {
        if (taskIsValid(task)) {
            getManagerTasksMap().put(task.getId(), task);
            orderedTasksSet.add(task);
        }
    }

    private void createSubTaskWhileLoad(SubTask subtask) {
        if (taskIsValid(subtask)) {
            getManagerSubTasksMap().put(subtask.getId(), subtask);
            getManagerEpicsMap().get(subtask.getEpicId()).refreshEpicData();
            orderedTasksSet.add(subtask);
        }
    }

    private void createEpicWhileLoad(Epic epic) {
        getManagerEpicsMap().put(epic.getId(), epic);
    }

    private static HTTPTaskManager loadFromClient(KvTaskClient client) {

        HTTPTaskManager loadedManager = new HTTPTaskManager(client.getUrl());

        Type TaskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = new Gson().fromJson(client.load("tasksKey"), TaskListType);

        Type EpicListType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = new Gson().fromJson(client.load("epicsKey"), EpicListType);

        Type SubTaskListType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> subTasks = new Gson().fromJson(client.load("subTasksKey"), SubTaskListType);

        Type historylistType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> history = new Gson().fromJson(client.load("historyKey"), historylistType);

        for (Task task : tasks) {
            loadedManager.createTaskWhileLoad(task);
        }
        for (Epic epic : epics) {
            loadedManager.createEpicWhileLoad(epic);
        }
        for (SubTask subTask : subTasks) {
            loadedManager.createSubTaskWhileLoad(subTask);
        }
        loadedManager.setUin(loadedManager.getManagerEpicsMap().size() + loadedManager.getManagerTasksMap().size()
                + loadedManager.getManagerSubTasksMap().size());
        for (int i = 0; i < history.size(); i++) {
            loadedManager.getMemoryManager().add(loadedManager.getTaskUniversal(history.get(i)));
        }
        return loadedManager;
    }
    public static void main(String[] args) {
        try {
            new KVServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTTPTaskManager manager =  (HTTPTaskManager) Managers.getDefault();
        Epic epc1 = new Epic("epc1", "this is epic N1");
        Epic epc2 = new Epic("epc2", "this is epic N2");
        manager.createEpic(epc1);
        manager.createEpic(epc2);
        SubTask sbt1 = new SubTask("sbt1", "this is subtask1", epc1.getId(),
                LocalDateTime.of(2022, 4, 16, 10, 0), Duration.ofMinutes(60));

        SubTask sbt2 = new SubTask("sbt2", "this is subtask2", epc1.getId(),
                LocalDateTime.of(2022,4,16,11,0), Duration.ofMinutes(60));

        SubTask sbt3 = new SubTask("sbt3", "this is subtask3", epc2.getId(),
                LocalDateTime.of(2022, 4,16,12,0), Duration.ofMinutes(60));

        SubTask sbt4 = new SubTask("sbt4", "this is subtask4", epc2.getId(),
                LocalDateTime.of(2022,4,16,13,0), Duration.ofMinutes(60));


        manager.createSubTask(sbt1, sbt1.getEpicId());
        manager.createSubTask(sbt2, sbt2.getEpicId());
        manager.createSubTask(sbt3, sbt3.getEpicId());
        manager.createSubTask(sbt4, sbt4.getEpicId());

        manager.getEpicByUin(epc1.getUin());
        manager.getEpicByUin(epc2.getUin());
        manager.getSubTaskByUin(sbt4.getUin());
        manager.getSubTaskByUin(sbt3.getUin());
        manager.getSubTaskByUin(sbt2.getUin());
        manager.getSubTaskByUin(sbt1.getUin());

        Task task = new Task("Taskname", "Taskdescription");
        task.setStartTime(LocalDateTime.of(2024,2,3,12,0));
        task.setStatus(Status.DONE);
        task.setDuration(Duration.ofMinutes(60));
        manager.createTask(task);
        manager.updateTask(task);
        manager.getTaskByUin(7);


        Task task2 = new Task("Taskname2", "Taskdescription2");
        task2.setStartTime(LocalDateTime.of(2055,11,3,12,0));
        task2.setStatus(Status.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(120));
        manager.createTask(task2);
        manager.updateTask(task2);
        manager.getTaskByUin(8);

        System.out.println(manager.client.load("tasksKey"));
        System.out.println(manager.client.load("epicsKey"));
        System.out.println(manager.client.load("subTasksKey"));
        System.out.println(manager.client.load("historyKey"));

        HTTPTaskManager newManager = loadFromClient(manager.client);
        String firstManager = manager.getMemoryManager().getTasks().toString();
        String secondManager = newManager.getMemoryManager().getTasks().toString();
        System.out.println(firstManager.equals(secondManager));
        System.out.println(firstManager);
        System.out.println(secondManager);
    }

}