package com.nemitha.systembackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class TicketPool {
    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);
    private final ArrayBlockingQueue<Integer> tickets;
    private final AtomicInteger totalTicketsProduced = new AtomicInteger(0);
    private final int maxCapacity;
    private final int totalTickets;
    private volatile boolean isRunning = true;
    private final Random random = new Random();

    // Constructor initializes the ticket pool
    public TicketPool(int maxCapacity, int totalTickets) {
        this.tickets = new ArrayBlockingQueue<>(maxCapacity);
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    // Synchronized method for vendors to add tickets
    public synchronized void addTickets(int vendorId) throws InterruptedException {

        // Generate random batch size between 1-5
        int ticketsToAdd = random.nextInt(5) + 1;

        // Wait if adding tickets would exceed capacity
        while (isRunning && (tickets.size() + ticketsToAdd) > maxCapacity) {
            logger.info("Vendor {} waiting - not enough space for {} tickets (Current size: {})",
                    vendorId, ticketsToAdd, tickets.size());
            wait();
        }

        // Check if system stopped while waiting
        if (!isRunning) return;

        // Track actual tickets added in this operation
        int actualAdded = 0;

        // Add tickets up to batch size or total limit
        for (int i = 0; i < ticketsToAdd; i++) {
            if (totalTicketsProduced.get() < totalTickets) {

                // Increment counter and add new ticket
                tickets.add(totalTicketsProduced.incrementAndGet());
                actualAdded++;
            } else {
                break;
            }
        }

        // Log success and notify waiting threads
        if (actualAdded > 0) {
            logger.info("Vendor {} added {} tickets. Pool size: {}/{}",
                    vendorId, actualAdded, tickets.size(), maxCapacity);
            notifyAll();
        }
    }

    // Synchronized method for customers to remove tickets
    public synchronized List<Integer> removeTickets(int customerId) throws InterruptedException {
        // Generate random purchase size between 1-3
        int ticketsToBuy = random.nextInt(3) + 1;
        List<Integer> boughtTickets = new ArrayList<>();

        // Wait if pool is empty
        while (isRunning && tickets.isEmpty()) {
            logger.info("Customer {} waiting - pool empty", customerId);
            wait();
        }

        // Check if system stopped while waiting
        if (!isRunning) return boughtTickets;

        // Purchase requested number of tickets if available
        for (int i = 0; i < ticketsToBuy && !tickets.isEmpty(); i++) {
            Integer ticket = tickets.poll();
            if (ticket != null) {
                boughtTickets.add(ticket);
            }
        }

        // Log success and notify waiting threads
        if (!boughtTickets.isEmpty()) {
            logger.info("Customer {} bought {} tickets {}. Remaining tickets: {}",
                    customerId, boughtTickets.size(), boughtTickets, tickets.size());
            notifyAll();
        }

        return boughtTickets;
    }

    // Stop all operations
    public void stop() {
        isRunning = false;
        synchronized (this) {
            notifyAll();
        }
    }

    // Check if all tickets have been produced and sold
    public boolean isComplete() {
        return totalTicketsProduced.get() >= totalTickets && tickets.isEmpty();
    }
}
