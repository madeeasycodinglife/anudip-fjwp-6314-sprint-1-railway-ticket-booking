package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainRequestDTO {
    private String trainName;
    private String startingStation;
    private String endingStation;
    private String trainDescription;

    private List<TrainClassRequestDTO> classes;
    private List<TrainStopRequestDTO> stops;
}
