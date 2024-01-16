package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PassengerRequestDTO {
    private String passengerName;
    private int age;
}
