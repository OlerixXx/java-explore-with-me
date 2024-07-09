package ru.practicum.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FutureOrTwoHoursLaterValidator implements ConstraintValidator<FutureOrTwoHoursLater, LocalDateTime> {

    @Override
    public void initialize(FutureOrTwoHoursLater constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values are considered valid
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursLater = now.plusHours(2);
        return value.isAfter(twoHoursLater);
    }
}
