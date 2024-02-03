package com.madeeasy.service;

import com.madeeasy.dto.request.TicketRequestDTO;
import org.springframework.http.ResponseEntity;

public interface TicketService {
    ResponseEntity<?> bookNextAvailableSeats(int numberOfTickets, TicketRequestDTO ticketRequestDTO);

    ResponseEntity<?> getAllTicketsByTrainNumber(String trainNumber);

    ResponseEntity<?> checkTicketsStatusByPnrNumber(String pnrNumber);

    ResponseEntity<?> cancelTicketsByPnrNumber(String pnrNumber);
}
