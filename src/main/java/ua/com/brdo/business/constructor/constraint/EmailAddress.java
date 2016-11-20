package ua.com.brdo.business.constructor.constraint;

import org.hibernate.validator.constraints.Email;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Email(message = "Please provide a valid e-mail address.")
@Pattern(regexp = ".+@.+\\..+", message = "Incorrect format of e-mail.")
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface EmailAddress {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
