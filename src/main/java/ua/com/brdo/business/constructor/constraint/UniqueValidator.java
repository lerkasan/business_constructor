package ua.com.brdo.business.constructor.constraint;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private ApplicationContext applicationContext;

    private JpaRepository repository;

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
        annotatedObjectName = annotatedObjectName.substring(0, 1).toLowerCase() + annotatedObjectName.substring(1);
        switch (annotatedObjectName) {
            case "user":
            case "businessType":
            case "questionnaire":
                if (applicationContext != null) {
                    repository = this.applicationContext.getBean(annotatedObjectName + "Repository", JpaRepository.class);
                }
                break;
            default:
                throw new IllegalArgumentException("Unexpected object class was passed to Unique annotation.");
        }
    }

    @SneakyThrows
    private boolean isValid(String param) {
        Method countByMethod = repository.getClass().getMethod("countBy" + field + "IgnoreCase", String.class);
        int counter = (int) countByMethod.invoke(repository, param);
        return counter == 0;
    }

    @SneakyThrows
    @Override
    public boolean isValid(String param, ConstraintValidatorContext context) {
        return isNull(applicationContext) || isNull(repository) || isNull(param) || isValid(param);
    }
}
