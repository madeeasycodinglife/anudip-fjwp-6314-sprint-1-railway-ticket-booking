package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlatformResponseDTO {
    private String platformNumber;
    private String description;
}
