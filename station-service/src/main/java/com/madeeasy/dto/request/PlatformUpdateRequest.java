package com.madeeasy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformUpdateRequest {

    @NotNull(message = "platforms can not be null")
    List<PlatformRequestDTO> platforms;
}
