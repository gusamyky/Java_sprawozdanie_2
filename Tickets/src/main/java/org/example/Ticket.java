package org.example;

public class Ticket {
    public int id;
    public boolean isReserved;
    public int reservedByThread;

    public Ticket(int id) {
        this.id = id;
        this.isReserved = false;
        this.reservedByThread = -1;
    }

    @Override
    public String toString() {
        return "Bilet nr " + id;
    }
}