package com.madeeasy.controller;

import com.madeeasy.dto.request.TicketRequestDTO;
import com.madeeasy.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket-service")
@RequiredArgsConstructor
@CrossOrigin("*")
@Validated
public class TicketServiceController {

    private final TicketService ticketService;

    @Operation(
            summary = "Book available seats",
            description = "Book available seats",
            tags = {"Book Tickets"}
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/book/tickets/{number-of-tickets}")
    public ResponseEntity<?> bookNextAvailableSeats(
            @PathVariable("number-of-tickets") @Min(value = 1) int numberOfTickets,
            @RequestBody TicketRequestDTO ticketRequestDTO) {
        return this.ticketService.bookNextAvailableSeats(numberOfTickets, ticketRequestDTO);
    }

    @Operation(
            summary = "Get all tickets by train number",
            description = "Get all tickets by train number",
            tags = {"Get List of  Tickets"}
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")
    @GetMapping("/get-all/by/train-number/{train-number}")
    public ResponseEntity<?> getAllTicketsByTrainNumber(
            @PathVariable("train-number") String trainNumber) {
        return this.ticketService.getAllTicketsByTrainNumber(trainNumber);
    }

    @Operation(
            summary = "Check tickets status by pnr number",
            description = "Check tickets status by pnr number",
            tags = {"Check Ticket Status"}
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/check-tickets-status/by/pnr-number/{pnr-number}")
    public ResponseEntity<?> checkTicketsStatusByPnrNumber(
            @PathVariable("pnr-number") String pnrNumber) {
        return this.ticketService.checkTicketsStatusByPnrNumber(pnrNumber);
    }
}
