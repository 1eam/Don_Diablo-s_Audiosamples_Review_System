package com.esther.dds.domain.Validator;

import com.esther.dds.domain.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch,Object> {
    private String baseField;
    private String matchField;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object baseFieldValue = getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            return baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
        } catch (Exception e) {
            // log error
            return false;
        }
    }

    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        baseField = constraintAnnotation.baseField();
        matchField = constraintAnnotation.matchField();
    }
}