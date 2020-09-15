package com.season.util;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanValidator {

    public static <T> void validate(T var) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(var);
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return;
        }
        throw new ValidationException(constraintViolations.iterator().next().getMessage());
    }
}
