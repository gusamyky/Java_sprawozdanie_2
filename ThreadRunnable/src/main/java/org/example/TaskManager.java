package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {
    private final Map<Integer, Thread> threads = new HashMap<>();
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final AtomicInteger idGen = new AtomicInteger(1);

    public int submitTask() {
        int id = idGen.getAndIncrement();
        Task task = new Task(id);
        Thread t = new Thread(task, "Task-" + id);
        tasks.put(id, task);
        threads.put(id, t);
        t.start();
        return id;
    }

    public List<Task> listTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Thread getThread(int id) {
        return threads.get(id);
    }

    public boolean cancelTask(int id) {
        Thread t = threads.get(id);
        if (t != null && t.isAlive()) {
            t.interrupt();
            return true;
        }
        return false;
    }
}
