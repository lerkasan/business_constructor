package ua.com.brdo.business.constructor.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoCycleValidator.class)
public @interface NoCycle {
    String message() default "{NoCycle}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
