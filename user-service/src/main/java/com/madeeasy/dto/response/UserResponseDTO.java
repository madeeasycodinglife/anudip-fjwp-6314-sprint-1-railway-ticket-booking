package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private AddressResponseDTO address;
    private List<String> roles;
}
