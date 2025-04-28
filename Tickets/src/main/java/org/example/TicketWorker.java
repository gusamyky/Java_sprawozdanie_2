package org.example;

public class TicketWorker implements Runnable {
    private final int threadId;
    private final TicketPool pool;
    private volatile WorkerRunningStatus status = WorkerRunningStatus.RUNNING;
    private volatile Ticket currentTicket = null;

    public TicketWorker(int id, TicketPool pool) {
        this.threadId = id;
        this.pool = pool;
    }

    @Override
    public void run() {
        while (status != WorkerRunningStatus.STOPPING) {
            currentTicket = pool.tryReserve(threadId);

            if (currentTicket == null) {
                status = WorkerRunningStatus.WAITING;
                try {
                    Thread.sleep(10000); // Czeka na dostępny bilet
                } catch (InterruptedException e) {
                    break;
                }
                continue;
            }

            status = WorkerRunningStatus.RUNNING;

            try {
                Thread.sleep((int)(Math.random() * 30000));
            } catch (InterruptedException e) {
                break;
            }

            pool.cancelReservation(currentTicket, threadId);
            currentTicket = null;

            try {
                Thread.sleep((int)(Math.random() * 30000));
            } catch (InterruptedException e) {
                break;
            }
        }

        status = WorkerRunningStatus.STOPPED;
        System.out.println("Wątek nr " + threadId + " zakończył działanie.");
    }

    public void stop() {
        status = WorkerRunningStatus.STOPPING;
    }

    public void start() {
        if (status == WorkerRunningStatus.STOPPED) {
            status = WorkerRunningStatus.RUNNING;
            new Thread(this).start();
        }
    }

    public int getThreadId() {
        return threadId;
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public WorkerRunningStatus getStatus() {
        return status;
    }

    public String getTicketInfo() {
        return currentTicket != null ? currentTicket.toString() : "-";
    }
}