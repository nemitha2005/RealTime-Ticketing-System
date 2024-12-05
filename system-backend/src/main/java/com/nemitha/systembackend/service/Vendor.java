package com.nemitha.systembackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vendor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseRate;
    private volatile boolean isRunning = true;

    // Constructor
    public Vendor(TicketPool ticketPool, int vendorId, int releaseRate) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        // Continue until stopped or all tickets produced
        while (isRunning && !ticketPool.isComplete()) {
            try {
                // Attempt to add tickets
                ticketPool.addTickets(vendorId);

                // Control release rate
                Thread.sleep(1000 / releaseRate);
            } catch (InterruptedException e) {
                logger.error("Vendor {} interrupted: {}", vendorId, e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Vendor {} stopping", vendorId);
    }

    // Stop this vendor's operations
    public void stop() {
        isRunning = false;
    }
}