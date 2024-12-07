package com.nemitha.systembackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemitha.systembackend.config.TicketingConfig;
import com.nemitha.systembackend.service.TicketingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ConfigurationController {

    // File path to store the configuration
    private static final String CONFIG_FILE = "src/main/resources/config.json";

    // Jackson object mapper for JSON serialization/deserialization
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Stores the current configuration
    private final TicketingService ticketingService;

    // Load the configuration from the file at the start
    private TicketingConfig currentConfig;

    //
    public ConfigurationController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try {
                // Load the configuration from the file into the currentConfig
                currentConfig = objectMapper.readValue(configFile, TicketingConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load configuration on startup: " + e.getMessage());
            }
        } else {
            currentConfig = new TicketingConfig(0, 0, 0, 0);
        }
    }

    // Endpoint to get the current configuration
    @GetMapping("/config")
    public TicketingConfig getConfig() {
        return currentConfig;
    }

    // Endpoint to update the configuration and save it to the file
    @PostMapping("/config")
    public ResponseEntity<String> updateConfig(@Valid @RequestBody TicketingConfig config) {
        currentConfig = config; // Update configuration if valid only
        try {
            objectMapper.writeValue(new File(CONFIG_FILE), currentConfig);
            return ResponseEntity.ok("Configuration updated successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to update configuration: " + e.getMessage());
        }
    }

    // Endpoint to start the ticketing system
    @PostMapping("/start")
    public ResponseEntity<String> startSystem() {
        if (currentConfig == null) {
            return ResponseEntity.badRequest()
                    .body("Configuration not set");
        }
        ticketingService.startSystem(currentConfig);
        return ResponseEntity.ok("System started successfully");
    }

    // Endpoint to stop the ticketing system
    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        ticketingService.stopSystem();
        return ResponseEntity.ok("System stopped successfully");
    }
}