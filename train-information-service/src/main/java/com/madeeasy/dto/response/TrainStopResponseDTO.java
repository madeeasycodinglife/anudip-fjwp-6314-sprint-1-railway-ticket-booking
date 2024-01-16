package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainStopResponseDTO {

    private String stopId;
    private String stationName;
    private String arrivalTime;
    private String departureTime;
}
