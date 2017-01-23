package ua.com.brdo.business.constructor.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.Payload;

@Target(value = {TYPE, ANNOTATION_TYPE /*, PARAMETER, METHOD */})
@Retention(value = RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String field();

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ANNOTATION_TYPE /*, PARAMETER, METHOD */})
    @Retention(RUNTIME)
    @interface List {

        Unique[] value();

//        ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;
    }
}
