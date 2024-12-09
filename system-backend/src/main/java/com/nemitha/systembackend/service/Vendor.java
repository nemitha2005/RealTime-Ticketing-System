package com.nemitha.systembackend.service;

import com.nemitha.systembackend.FrontendService.LogStreamingController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vendor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Vendor.class);
    private final LogStreamingController logStreamingController;
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releaseRate;
    private volatile boolean isRunning = true;

    // Constructor
    public Vendor(TicketPool ticketPool, int vendorId, int releaseRate, LogStreamingController logStreamingController) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.logStreamingController = logStreamingController;
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
                logStreamingController.broadcastLog("Vendor " + vendorId + " interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Vendor {} stopping", vendorId);
        logStreamingController.broadcastLog("Vendor " + vendorId + " stopping.");
    }

    // Stop this vendor's operations
    public void stop() {
        isRunning = false;
    }
}