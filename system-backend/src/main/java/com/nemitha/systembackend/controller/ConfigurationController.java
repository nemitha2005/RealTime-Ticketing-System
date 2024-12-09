package com.nemitha.systembackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemitha.systembackend.config.TicketingConfig;
import com.nemitha.systembackend.service.TicketingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ConfigurationController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
    private static final String CONFIG_FILE = "src/main/resources/config.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TicketingService ticketingService;
    private TicketingConfig currentConfig;

    public ConfigurationController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
        loadConfiguration();
    }

    private void loadConfiguration() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                currentConfig = objectMapper.readValue(configFile, TicketingConfig.class);
                logger.info("Configuration loaded: {}", currentConfig);
            } catch (IOException e) {
                logger.error("Failed to load configuration on startup: {}", e.getMessage());
                currentConfig = new TicketingConfig(0, 0, 0, 0);
            }
        } else {
            currentConfig = new TicketingConfig(0, 0, 0, 0);
            logger.info("No configuration file found, using default values");
        }
    }

    @GetMapping("/config")
    public TicketingConfig getConfig() {
        return currentConfig;
    }

    @PostMapping("/config")
    public ResponseEntity<String> updateConfig(@Valid @RequestBody TicketingConfig config) {
        // If system is running, we can't update the configuration
        if (ticketingService.isRunning()) {
            return ResponseEntity.badRequest()
                    .body("Cannot update configuration while system is running. Please stop the system first.");
        }

        try {
            // Save the new configuration to file
            objectMapper.writeValue(new File(CONFIG_FILE), config);
            currentConfig = config;
            logger.info("Configuration updated successfully: {}", config);
            return ResponseEntity.ok("Configuration updated successfully");
        } catch (IOException e) {
            logger.error("Failed to update configuration: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Failed to update configuration: " + e.getMessage());
        }
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        // Check if configuration is valid
        if (currentConfig == null || !isConfigValid(currentConfig)) {
            return ResponseEntity.badRequest()
                    .body("Invalid or missing configuration");
        }

        // Check if system is already running
        if (ticketingService.isRunning()) {
            return ResponseEntity.badRequest()
                    .body("System is already running. Please stop it first.");
        }

        try {
            ticketingService.startSystem(currentConfig);
            logger.info("System started with configuration: {}", currentConfig);
            return ResponseEntity.ok("System started successfully");
        } catch (Exception e) {
            logger.error("Failed to start system: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Failed to start system: " + e.getMessage());
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        if (!ticketingService.isRunning()) {
            return ResponseEntity.badRequest()
                    .body("System is not running");
        }

        try {
            ticketingService.stopSystem();
            logger.info("System stopped successfully");
            return ResponseEntity.ok("System stopped successfully");
        } catch (Exception e) {
            logger.error("Failed to stop system: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Failed to stop system: " + e.getMessage());
        }
    }

    // Helper method to validate configuration
    private boolean isConfigValid(TicketingConfig config) {
        return config.getTotalTickets() > 0 &&
                config.getTicketReleaseRate() > 0 &&
                config.getCustomerRetrievalRate() > 0 &&
                config.getMaxTicketCapacity() > 0;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getSystemStatus() {
        boolean isRunning = ticketingService.isRunning();
        return ResponseEntity.ok(isRunning ? "System is running" : "System is stopped");
    }
}