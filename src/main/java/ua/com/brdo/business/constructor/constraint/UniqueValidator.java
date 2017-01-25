package ua.com.brdo.business.constructor.constraint;

import static java.util.Objects.isNull;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    @Autowired
    private ApplicationContext applicationContext;
    private UniqueValidatable service;
    private String fieldName;

    public void initialize(Unique annotation) {
        Class<? extends UniqueValidatable> serviceCls = annotation.service();
        if (applicationContext != null) {
            service = applicationContext.getBean(serviceCls);
        }
        fieldName = annotation.field();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object annotatedObject, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(service) || isNull(annotatedObject) || isValid(annotatedObject);
    }

    @SneakyThrows
    private boolean isValid(Object annotatedObject) {
        Long id = 0L;
        Objects.requireNonNull(annotatedObject);
        String fieldValue = BeanUtils.getProperty(annotatedObject, fieldName);
        String idStr = BeanUtils.getProperty(annotatedObject, "id");
        if (idStr != null) {
            id = Long.valueOf(idStr);
        }
        return isNull(fieldValue) || service.isAvailable(fieldName, fieldValue, id);
    }
}
