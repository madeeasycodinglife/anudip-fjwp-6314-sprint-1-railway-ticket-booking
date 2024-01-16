package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForgotPasswordRequestDTO {
    private String email;
}
