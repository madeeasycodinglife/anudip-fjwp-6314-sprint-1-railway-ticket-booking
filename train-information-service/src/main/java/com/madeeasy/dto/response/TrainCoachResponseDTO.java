package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TrainCoachResponseDTO {
    private String coachId;
    private String name;
    private int seats;
    private int availableSeats;
}
