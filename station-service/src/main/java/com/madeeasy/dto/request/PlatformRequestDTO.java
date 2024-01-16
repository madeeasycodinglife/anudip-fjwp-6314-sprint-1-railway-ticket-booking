package com.madeeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformRequestDTO {
    @NotBlank(message = "Platform number is required")
    private String platformNumber;
    private String description;
    // Add any other relevant fields
}
