package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TicketRequestDTO {
    private String seatClass;
    private String trainNumber;
    private String date;
    private List<PassengerRequestDTO> passengers;
}
