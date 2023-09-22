package ru.yandex.practicum.filmorate.validation.annotations;

import ru.yandex.practicum.filmorate.validation.ReleaseDateConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.*;
import java.lang.annotation.*;

/**
 * аннотация для проверки поля дата выхода фильма на соответствие ограничению
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
@NotNull
@Constraint(validatedBy = ReleaseDateConstraintValidator.class)
public @interface CheckReleaseDate {
	String message() default "{value.invalid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
