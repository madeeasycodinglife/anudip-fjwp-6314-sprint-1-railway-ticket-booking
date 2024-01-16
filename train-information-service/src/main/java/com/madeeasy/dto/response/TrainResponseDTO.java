package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainResponseDTO {
    private String trainId;
    private String trainName;
    private String trainNumber;
    private String startingStation;
    private String endingStation;
    private String trainDescription;

    private List<TrainClassResponseDTO> classes;
    private List<TrainStopResponseDTO> stops;
}
