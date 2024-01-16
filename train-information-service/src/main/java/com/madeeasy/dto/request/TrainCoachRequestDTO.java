package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainCoachRequestDTO {

        private String name;
        private int seats;
}
