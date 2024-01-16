package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;

    private AddressRequestDTO address;
    private List<String> roles;
}
