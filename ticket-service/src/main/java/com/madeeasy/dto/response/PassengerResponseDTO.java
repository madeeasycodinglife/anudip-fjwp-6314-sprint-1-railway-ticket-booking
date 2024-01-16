package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerResponseDTO {
    private String passengerId;
    private String passengerName;
    private int age;
    private int seatNumber;
}
