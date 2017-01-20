package ua.com.brdo.business.constructor.constraint;

import static java.util.Objects.isNull;

import java.lang.reflect.Method;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
                break;
            default:
                throw new IllegalArgumentException("Unexpected field was passed to Unique annotation.");
        }

        String annotatedObjectName = annotation.object().getSimpleName();
        annotatedObjectName = annotatedObjectName.substring(0,1).toLowerCase() + annotatedObjectName.substring(1);
        switch (annotatedObjectName) {
            case "user":
            case "businessType":
            case "questionnaire":
                if (applicationContext != null) {
                    service = this.applicationContext.getBean(annotatedObjectName + "ServiceImpl", UniqueValidatable.class);
                    Advised advisedService = (Advised) service;
                    Class<?> serviceCls = advisedService.getTargetSource().getTargetClass();
                    if (!UniqueValidatable.class.isAssignableFrom(serviceCls)) {
                        new ReflectiveOperationException("Service " + serviceCls.getSimpleName()
                            + " should implement interface " + UniqueValidatable.class.getSimpleName()
                            + " in order to maintain correct processing of annotation Unique for entity fields.");
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected object class was passed to Unique annotation.");
        }
    }

    @SneakyThrows
    private boolean isValid(String param) {
        String methodName = "isAvailable";
        Method isAvailableMethod = service.getClass().getMethod(methodName, String.class, String.class);
        return (boolean) isAvailableMethod.invoke(service, field, param);
    }

    @SneakyThrows
    @Override
    public boolean isValid(String param, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(service) || isNull(param) || isValid(param);
    }
}
