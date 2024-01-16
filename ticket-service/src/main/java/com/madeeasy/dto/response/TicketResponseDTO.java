package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TicketResponseDTO {
    private String pnrNumber;
    private String source;
    private String destination;
    private String seatClass;
    private String seatNumber;
    private String trainNumber;
    private String date;
    private String arrivalTime;
    private String departureTime;
    private List<PassengerResponseDTO> passengers;
}
