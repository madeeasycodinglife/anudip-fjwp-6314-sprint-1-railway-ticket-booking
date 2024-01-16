package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainClassRequestDTO {

    private String className;
    private int totalSeats;
    private List<TrainCoachRequestDTO> coaches;
}
