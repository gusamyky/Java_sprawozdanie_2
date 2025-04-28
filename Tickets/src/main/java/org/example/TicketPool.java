package org.example;

import java.util.*;

public class TicketPool {
    private final List<Ticket> tickets = new ArrayList<>();

    public TicketPool(int count) {
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(i));
        }
    }

    public synchronized Ticket tryReserve(int threadId) {
        for (Ticket ticket : tickets) {
            if (!ticket.isReserved) {
                ticket.isReserved = true;
                ticket.reservedByThread = threadId;
                System.out.println("Wątek nr " + threadId + " zarezerwował bilet nr " + ticket.id);
                return ticket;
            }
        }
        System.out.println("Wątek nr " + threadId + " nie znalazł wolnego biletu i czeka...");
        return null;
    }

    public synchronized void cancelReservation(Ticket ticket, int threadId) {
        if (ticket != null && ticket.reservedByThread == threadId) {
            ticket.isReserved = false;
            ticket.reservedByThread = -1;
            System.out.println("Wątek nr " + threadId + " zwolnił rezerwację biletu nr " + ticket.id);
        }
    }
}