package ua.com.brdo.business.constructor.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ua.com.brdo.business.constructor.validator.EmailAddressValidator;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailAddressValidator.class)
public @interface EmailAddress {
    String message() default "{ua.com.brdo.business.constructor.constraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
