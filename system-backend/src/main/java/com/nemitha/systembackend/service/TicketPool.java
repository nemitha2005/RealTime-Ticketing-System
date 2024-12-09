package com.nemitha.systembackend.service;

import com.nemitha.systembackend.FrontendService.LogStreamingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketPool {
    // Logger for tracking operations and debugging
    private static final Logger logger = LoggerFactory.getLogger(TicketPool.class);

    // Log broadcasting controller
    private final LogStreamingController logStreamingController;

    // Custom Ticket class to track vendor information
    public static class VendorTicket {
        private final int ticketNumber;

        public VendorTicket(int ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        @Override
        public String toString() {
            return "Ticket " + ticketNumber;
        }
    }

    private final ArrayBlockingQueue<VendorTicket> tickets;
    private final AtomicInteger totalTicketsProduced = new AtomicInteger(0);
    private final int maxCapacity;
    private final int totalTickets;
    private volatile boolean isRunning = true;

    public TicketPool(int maxCapacity, int totalTickets, LogStreamingController logStreamingController) {
        this.tickets = new ArrayBlockingQueue<>(maxCapacity);
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.logStreamingController = logStreamingController;
    }

    // Synchronized method for vendors to add tickets
    public synchronized void addTickets(int vendorId) throws InterruptedException {
        // Wait if adding tickets would exceed capacity
        while (isRunning && (tickets.size()) >= maxCapacity) {
            logger.info("Vendor {} waiting - not enough space for ticket (Current size: {})",
                    vendorId, tickets.size());
            logStreamingController.broadcastLog("Vendor " + vendorId + " waiting - not enough space for ticket (Current size: " + tickets.size() + ")");
            wait(); // Release lock and wait for space
        }

        // Check if system stopped while waiting
        if (!isRunning) return;

        // Add only one ticket if total tickets not exceeded
        if (totalTicketsProduced.get() < totalTickets) {
            // Create a new ticket with vendor tracking
            int newTicketNumber = totalTicketsProduced.incrementAndGet();
            VendorTicket newTicket = new VendorTicket(newTicketNumber);

            tickets.add(newTicket);

            logger.info("Vendor {} added ticket {}. Pool size: {}/{}",
                    vendorId, newTicket, tickets.size(), maxCapacity);
            logStreamingController.broadcastLog("Vendor " + vendorId + " added ticket " + newTicket + ". Pool size: " + tickets.size() + "/" + maxCapacity);
            notifyAll(); // Wake up waiting customers
        }
    }

    // Synchronized method for customers to remove tickets
    public synchronized List<VendorTicket> removeTickets(int customerId) throws InterruptedException {
        List<VendorTicket> boughtTickets = new ArrayList<>();

        // Wait if pool is empty
        while (isRunning && tickets.isEmpty()) {
            logger.info("Customer {} waiting - pool empty", customerId);
            logStreamingController.broadcastLog("Customer " + customerId + " waiting - pool empty");
            wait(); // Release lock and wait for tickets
        }

        // Check if system stopped while waiting
        if (!isRunning) return boughtTickets;

        // Remove exactly one ticket
        VendorTicket ticket = tickets.poll();
        if (ticket != null) {
            boughtTickets.add(ticket);

            logger.info("Customer {} bought ticket {}. Remaining tickets: {}",
                    customerId, ticket, tickets.size());
            logStreamingController.broadcastLog("Customer " + customerId + " bought ticket " + ticket + ". Remaining tickets: " + tickets.size());
            notifyAll(); // Wake up waiting vendors
        }

        return boughtTickets;
    }

    // Stop all operations
    public void stop() {
        isRunning = false;
        synchronized (this) {
            notifyAll(); // Wake up all waiting threads
        }
    }

    // Check if all tickets have been produced and sold
    public boolean isComplete() {
        return totalTicketsProduced.get() >= totalTickets && tickets.isEmpty();
    }
}