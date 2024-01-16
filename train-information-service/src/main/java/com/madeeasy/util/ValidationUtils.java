package com.madeeasy.util;

import com.madeeasy.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    public static void validate(Object object) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            Map<String, String> errors = violations.stream()
                    .collect(Collectors.toMap(
                            violation -> StringUtils.substringAfterLast(violation.getPropertyPath().toString(), "."),
                            ConstraintViolation::getMessage
                    ));
            throw new ValidationException(errors);
        }
    }
}

