package ua.com.brdo.business.constructor.constraint;

import static java.util.Objects.isNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
//@SupportedValidationTarget({ValidationTarget.PARAMETERS, ValidationTarget.ANNOTATED_ELEMENT})
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    private ApplicationContext applicationContext;
    private UniqueValidatable service;
    private String fieldName;

    public void initialize(Unique annotation) {
        fieldName = annotation.field();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object annotatedObject, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(annotatedObject) || isValid(annotatedObject);
    }

    @SneakyThrows
    private boolean isValid(Object annotatedObject) {
        Objects.requireNonNull(annotatedObject);
        Field fieldObj = annotatedObject.getClass().getDeclaredField(fieldName);
        Field idObj = annotatedObject.getClass().getDeclaredField("id");
        idObj.setAccessible(true);
        fieldObj.setAccessible(true);
        Long id = (Long) idObj.get(annotatedObject);
        String fieldValue = (String) fieldObj.get(annotatedObject);
        idObj.setAccessible(false);
        fieldObj.setAccessible(false);
        String methodName = "isAvailable";
        service = acquireService(annotatedObject);
        if (service == null) {
            return true;
        }
        Method isAvailableMethod = service.getClass()
            .getMethod(methodName, String.class, String.class, Long.class);
        boolean validationResult = (boolean) isAvailableMethod.invoke(service, fieldName, fieldValue, id);
        return validationResult;
    }

    private UniqueValidatable acquireService(Object annotatedObject) {
        String annotatedObjectName = annotatedObject.getClass().getSimpleName();
        // Name of service bean starts with lowercase letter by default for beans in Spring context.
        // But method getSimpleName returns class name starting with capital letter.
        // So it's necessary to convert the first letter of class name to lowercase.
        annotatedObjectName = annotatedObjectName.substring(0, 1).toLowerCase()
            + annotatedObjectName.substring(1);
        if (applicationContext != null) {
            service = this.applicationContext
                .getBean(annotatedObjectName + "ServiceImpl", UniqueValidatable.class);
        }
        return service;
    }
}
