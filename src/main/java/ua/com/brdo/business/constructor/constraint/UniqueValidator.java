package ua.com.brdo.business.constructor.constraint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;

import static java.util.Objects.isNull;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private ApplicationContext applicationContext;

    private UniqueValidatable service;

    private String field = "";

    public void initialize(Unique annotation) {
        switch (annotation.field()) {
            case "email":
            case "username":
            case "title":
            case "codeKved":
                field = annotation.field();
                field = field.substring(0, 1).toUpperCase() + field.substring(1);
                break;
            default:
                throw new IllegalArgumentException("Unexpected field was passed to Unique annotation.");
        }

        String annotatedObjectName = annotation.object().getSimpleName();
        switch (annotatedObjectName) {
            case "User":
            case "BusinessType":
            case "Questionnaire":
                if (applicationContext != null) {
                    service = this.applicationContext.getBean(annotatedObjectName.toLowerCase() + "ServiceImpl", UniqueValidatable.class);
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected object class was passed to Unique annotation.");
        }
    }

    @SneakyThrows
    private boolean isValid(String param) {
        Method isAvailableMethod = service.getClass().getMethod("is" + field + "Available", String.class);
        return (boolean) isAvailableMethod.invoke(service, param);
    }

    @SneakyThrows
    @Override
    public boolean isValid(String param, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(service) || isNull(param) || isValid(param);
    }
}
