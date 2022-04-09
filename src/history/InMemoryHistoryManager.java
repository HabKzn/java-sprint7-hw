package history;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    public Node<Task> head;
    public Node<Task> tail;
    private int size = 0;
    private Map<Integer, Node<Task>> map;

    public InMemoryHistoryManager() {
        map = new HashMap<>();
    }

    public void linkLast(Task element) {
        Node<Task> t = tail;
        Node<Task> newNode = new Node<>(element, null, t);
        tail = newNode;
        if (t == null)
            head = newNode;
        else
            t.next = newNode;
        size++;
    }

    void removeNode(Node<Task> node) {
        if (node.prev == null && node.next == null) {
            head = null;
            tail = null;
            size = 0;
            return;
        }
        if (node.prev == null) {
            head = node.next;
            node.next.prev = null;
        } else if (node.next == null) {
            tail = node.prev;
            node.prev.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }

    Node<Task> getLast(Node<Task> head) {
        Node<Task> temp = head;
        if (temp == null) {
            return null;
        }
        while (temp.next != null) {
            temp = temp.next;
        }
        return temp;
    }

    @Override
    public void add(Task task) {
        if (map.containsKey(task.getId())) {
            removeNode(map.get(task.getId()));
            remove(task.getId());
        }
        linkLast(task);
        map.put(task.getId(), getLast(head));
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
         Node<Task> thisNode = this.head;
        if(thisNode != null){
         while (thisNode.next != null) {
            tasks.add(thisNode.data);
            thisNode = thisNode.next;
        }
        tasks.add(thisNode.data);
        return tasks;}
        else return null;
    }

    @Override
    public void remove(int id) {
        map.remove(id);
    }
}
