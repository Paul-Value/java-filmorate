package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.exception.ValidException;

import java.time.LocalDate;

public class CorrectReleaseDateValidator implements ConstraintValidator<CorrectReleaseDate, LocalDate> {
    private LocalDate releaseDateStart;

    @Override
    public void initialize(CorrectReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        releaseDateStart = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (releaseDateStart == null) {
            throw new ValidException("Release Date can't be null");
        }
        return date.isAfter(releaseDateStart) && date.isBefore(LocalDate.now());
    }
}
