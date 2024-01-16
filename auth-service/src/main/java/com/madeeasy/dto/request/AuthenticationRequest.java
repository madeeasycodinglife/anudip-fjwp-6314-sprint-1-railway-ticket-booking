package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}
