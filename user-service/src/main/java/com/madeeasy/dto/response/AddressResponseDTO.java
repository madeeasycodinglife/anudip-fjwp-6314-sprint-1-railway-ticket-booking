package com.madeeasy.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDTO {
    private String street;
    private String city;
    private String state;
    private String country;
}
