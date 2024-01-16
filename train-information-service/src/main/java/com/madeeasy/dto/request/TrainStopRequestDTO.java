package com.madeeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainStopRequestDTO {

    @NotBlank(message = "Station name is required")
    private String stationName;
    @NotBlank(message = "Station name is required")
    private String arrivalTime;
    @NotBlank(message = "Station name is required")
    private String departureTime;
}
