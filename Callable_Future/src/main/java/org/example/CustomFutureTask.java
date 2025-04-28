package org.example;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CustomFutureTask<V> extends FutureTask<V> {
    private final int taskId;

    public CustomFutureTask(Callable<V> callable, int taskId) {
        super(callable);
        this.taskId = taskId;
    }

    public int getTaskId() {
        return taskId;
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            System.out.println("Task " + taskId + " was cancelled.");
        } else {
            System.out.println("Task " + taskId + " successfully done.");
        }
    }
}