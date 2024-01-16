package com.madeeasy.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private String name;
    private String email;
    private String password;
    private AddressResponseDTO address;
    private List<String> roles;
}
