package ua.com.brdo.business.constructor.constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value = {TYPE, ANNOTATION_TYPE})
@Retention(value = RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String field();

    Class<? extends UniqueValidatable> service();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @interface List {
        Unique[] value();
    }
}
