package history;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class HistoryManagerTest {
    Task task;
    Epic epic;
    SubTask subTask;
   HistoryManager manager;


    @BeforeEach
    void createManager () {
      manager = new InMemoryHistoryManager();
         task = new Task("name", "description", LocalDateTime.of(2022,04,16,10,0), Duration.ofMinutes(50));
        task.setUin(1);
       epic = new Epic("epicName", "epicDescription");
        epic.setUin(2);
       subTask = new SubTask("subTaskName", "subTaskDescription", epic, LocalDateTime.of(2022,4,16,10,50), Duration.ofMinutes(50));
        subTask.setUin(3);
    }

    @Test
    void addWhileHistoryEmpty() {
      Assertions.assertEquals(manager.getSize(), 0);
       manager.add(task);
       Assertions.assertEquals(manager.getSize(), 1);
    }

    @Test
    void addDuplication() {
        manager.add(task);
        manager.add(task);
        Assertions.assertEquals(manager.getSize(), 1);
        Assertions.assertEquals(manager.getTasks().get(0), task);
    }

    @Test
    void removeFirst() {
        manager.add(task);
        manager.add(subTask);
        manager.remove(task.getId());
        Assertions.assertEquals(manager.getTasks().get(0), subTask);
        Assertions.assertEquals(manager.getTasks().size(), 1);

    }

    @Test
    void removeMiddle() {
        manager.add(epic);
        manager.add(subTask);
        manager.add(task);
        Assertions.assertEquals(manager.getTasks().size(), 3);
        manager.remove(subTask.getId());
        Assertions.assertEquals(manager.getTasks().get(1), task);
        Assertions.assertEquals(manager.getTasks().size(), 2);
    }

    @Test
    void removeLast() {
        manager.add(epic);
        manager.add(subTask);
        manager.add(task);
        Assertions.assertEquals(manager.getTasks().size(), 3);
        manager.remove(task.getId());
        Assertions.assertEquals(manager.getTasks().size(), 2);
    }

    @Test
    void getTasksTest() {
        manager.add(epic);
        manager.add(subTask);
        manager.add(task);
        Assertions.assertEquals(manager.getTasks().size(), 3);
        Assertions.assertEquals(manager.getTasks().get(0), epic);
        Assertions.assertEquals(manager.getTasks().get(1), subTask);
        Assertions.assertEquals(manager.getTasks().get(2), task);

    }

    @Test
    void getTasksWhileHistoryIsEmpty() {
        Assertions.assertEquals(manager.getTasks().size(), 0);
    }

    @Test
    void getSizeIfManagerEmpty(){
        Assertions.assertEquals(manager.getSize(), 0);
    }

    @Test
    void getSizeIfManagerFilled(){
        manager.add(epic);
        manager.add(subTask);
        manager.add(task);
        Assertions.assertEquals(manager.getSize(), 3);
    }
}