package com.madeeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationUpdateRequestDTO {
    @NotBlank(message = "Station name cannot be empty")
    private String stationName;
    @NotBlank(message = "Station location cannot be empty")
    private String location;
    @NotBlank(message = "Station description cannot be empty")
    private String description;
}
