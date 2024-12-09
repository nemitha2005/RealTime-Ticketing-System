package com.nemitha.systembackend.service;

import com.nemitha.systembackend.FrontendService.LogStreamingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class Customer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);
    private final LogStreamingController logStreamingController;
    private final TicketPool ticketPool;
    private final int customerId;
    private final int retrievalRate;
    private volatile boolean isRunning = true;

    // Constructor
    public Customer(TicketPool ticketPool, LogStreamingController logStreamingController, int customerId, int retrievalRate) {
        this.ticketPool = ticketPool;
        this.logStreamingController = logStreamingController;
        this.customerId = customerId;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        // Continue until stopped or all tickets processed
        while (isRunning && !ticketPool.isComplete()) {
            try {
                // Attempt to buy tickets
                List<TicketPool.VendorTicket> tickets = ticketPool.removeTickets(customerId);
                if (tickets.isEmpty()) break;

                // Control retrieval rate
                Thread.sleep(1000 / retrievalRate);
            }
            catch (InterruptedException e) {
                logger.error("Customer {} interrupted: {}", customerId, e.getMessage());
                logStreamingController.broadcastLog("Customer " + customerId + " interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Customer {} stopping", customerId);
        logStreamingController.broadcastLog("Customer " + customerId + " stopping.");
    }

    // Stop this customer's operations
    public void stop() {
        isRunning = false;
    }
}
