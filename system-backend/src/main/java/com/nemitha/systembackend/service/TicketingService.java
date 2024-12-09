package com.nemitha.systembackend.service;

import com.nemitha.systembackend.FrontendService.LogStreamingController;
import com.nemitha.systembackend.config.TicketingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TicketingService {
    // Logger for tracking operations
    private static final Logger logger = LoggerFactory.getLogger(TicketingService.class);

    // Constants for thread counts
    private static final int NUM_VENDORS = 5;
    private static final int NUM_CUSTOMERS = 5;

    // Core components
    private TicketPool ticketPool;
    private List<Vendor> vendors;
    private List<Customer> customers;
    private ExecutorService executorService;
    private boolean isRunning = false;

    // Injected dependency
    private final LogStreamingController logStreamingController;

    public TicketingService(LogStreamingController logStreamingController) {
        this.logStreamingController = logStreamingController;
    }

    // Start the ticketing system
    public synchronized void startSystem(TicketingConfig config) {
        // Prevent multiple starts
        /*
        if (isRunning) {
            logger.warn("System is already running");
            return;
        }*/

        // Initialize components
        ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets(), logStreamingController);
        vendors = new ArrayList<>();
        customers = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(NUM_VENDORS + NUM_CUSTOMERS);

        // Start vendor threads
        for (int i = 1; i <= NUM_VENDORS; i++) {
            Vendor vendor = new Vendor(ticketPool, i,  config.getTicketReleaseRate(), logStreamingController);
            vendors.add(vendor);
            executorService.submit(vendor);
        }

        // Start customer threads
        for (int i = 1; i <= NUM_CUSTOMERS; i++) {
            Customer customer = new Customer(ticketPool, logStreamingController, i, config.getCustomerRetrievalRate());
            customers.add(customer);
            executorService.submit(customer);
        }

        isRunning = true;

        //logger.info("Ticketing system started with {} vendors and {} customers", NUM_VENDORS, NUM_CUSTOMERS);
    }

    // Stop the ticketing system
    public synchronized void stopSystem() {
        if (!isRunning) {
            logger.warn("System is not running");
            return;
        }

        // Stop all components
        ticketPool.stop();
        vendors.forEach(Vendor::stop);
        customers.forEach(Customer::stop);

        // Shutdown thread pool
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        isRunning = false;
        logger.info("Ticketing system stopped");
        logStreamingController.broadcastLog("Ticketing system stopped");

    }

    // Check system status
    public boolean isRunning() {
        return isRunning;
    }
}