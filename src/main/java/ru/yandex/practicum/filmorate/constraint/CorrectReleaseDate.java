package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {CorrectReleaseDateValidator.class})
public @interface CorrectReleaseDate {
    String value();

    String message() default "The release date is not earlier than a certain date and not later than today";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
