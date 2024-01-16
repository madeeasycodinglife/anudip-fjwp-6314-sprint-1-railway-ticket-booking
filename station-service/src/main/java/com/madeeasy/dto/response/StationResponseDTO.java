package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StationResponseDTO {
    private String stationCode;
    private String stationName;
    private String location;
    private String description;
    private List<PlatformResponseDTO> platforms;
    // Add any other relevant fields

}
