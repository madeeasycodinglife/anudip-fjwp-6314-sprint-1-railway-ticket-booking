package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrainClassResponseDTO {

    private String classId;
    private String className;
    private int totalSeats;
    private List<TrainCoachResponseDTO> coaches;
}
