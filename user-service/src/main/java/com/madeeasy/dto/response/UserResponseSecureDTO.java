package com.madeeasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseSecureDTO {
    private String name;
    private String email;
    private String password;
    private AddressResponseDTO address;
    private List<String> roles;
}
