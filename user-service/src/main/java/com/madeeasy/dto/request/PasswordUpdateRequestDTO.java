package com.madeeasy.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PasswordUpdateRequestDTO {
    private String oldPassword;
    private String newPassword;
}
