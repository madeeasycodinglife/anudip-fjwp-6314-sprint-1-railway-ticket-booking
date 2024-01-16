package com.madeeasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequestDTO {
    private String street;
    private String city;
    private String state;
    private String country;
}
