package com.myjourneyblog.MyJourneyBlog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * Validator for ValidEnum annotation
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }

        Enum<?>[] enumConstants = enumClass.getEnumConstants();

        return Arrays.stream(enumConstants)
                .anyMatch(e -> {
                    if (ignoreCase) {
                        return e.name().equalsIgnoreCase(value);
                    }
                    return e.name().equals(value);
                });
    }
}
