package ua.com.brdo.business.constructor.constraint;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;

import static java.util.Objects.isNull;
import static ua.com.brdo.business.constructor.constraint.UniqueValidatable.IS_AVAILABLE;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    private ApplicationContext applicationContext;
    private UniqueValidatable service;
    private Class<? extends UniqueValidatable> serviceCls;
    private String fieldName;

    public void initialize(Unique annotation) {
        fieldName = annotation.field();
        serviceCls = annotation.service();
        service = applicationContext.getBean(serviceCls);
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object annotatedObject, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(service) || isNull(annotatedObject) || isValid(annotatedObject);
    }

    @SneakyThrows
    private boolean isValid(Object annotatedObject) {
        Method isAvailableMethod;
        boolean validationResult;
        Objects.requireNonNull(annotatedObject);
        String fieldValue = BeanUtils.getProperty(annotatedObject, fieldName);
        String id = BeanUtils.getProperty(annotatedObject, "id");
        isAvailableMethod = serviceCls.getMethod(IS_AVAILABLE, String.class, String.class, Long.class);
        validationResult = (boolean) isAvailableMethod.invoke(service, fieldName, fieldValue, id);
        return validationResult;
    }
}
