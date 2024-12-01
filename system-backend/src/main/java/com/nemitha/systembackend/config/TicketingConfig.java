package com.nemitha.systembackend.config;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TicketingConfig {

    @Min(value = 1, message = "Total tickets should be greater than 0")
    private int totalTickets; // total number of tickets to be sold

    @Min(value = 1, message = "Ticket release rate should be greater than 0")
    private int ticketReleaseRate; // number of tickets to be released per second by vendors

    @Min(value = 1, message = "Customer retrieval rate should be greater than 0")
    private int customerRetrievalRate; // number of tickets to be retrieved per second by customers

    @Min(value = 1, message = "Max ticket capacity should be greater than 0")
    private int maxTicketCapacity; // maximum number of tickets that can be hold in the ticket pool
}
