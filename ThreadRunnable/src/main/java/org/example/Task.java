package org.example;

public class Task implements Runnable {
    public enum State { NEW, RUNNING, COMPLETED, CANCELLED, ERROR }

    private final int id;
    private volatile State state = State.NEW;
    private volatile boolean working = false;
    private String result = null;

    public Task(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public State getState() { return state; }
    public boolean isWorking() { return working; }
    public String getResult() { return result; }

    @Override
    public void run() {
        state = State.RUNNING;
        working = true;
        try {
            int n = 10;
            long sum = 0;
            for (int i = 1; i <= n; i++) {
                Thread.sleep(300);
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                sum += i;
            }
            result = "Suma 1.." + n + " = " + sum;
            state = State.COMPLETED;
        } catch (InterruptedException e) {
            state = State.CANCELLED;
        } catch (Exception e) {
            state = State.ERROR;
            result = "Błąd: " + e.getMessage();
        } finally {
            working = false;
        }
    }
}
