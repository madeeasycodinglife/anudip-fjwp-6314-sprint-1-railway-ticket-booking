package com.madeeasy.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StationRequestDTO {

    @NotBlank(message = "Station name cannot be empty")
    private String stationName;
    @NotBlank(message = "Station location cannot be empty")
    private String location;
    @NotBlank(message = "Station description cannot be empty")
    private String description;
    @Valid
    @NotNull(message = "Platform List cannot be null")
    private List<PlatformRequestDTO> platforms;
    // Add any other relevant fields
}
