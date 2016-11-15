package ua.com.brdo.business.constructor.constraint.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
/*@Configurable(preConstruction=true,dependencyCheck=true,autowire= Autowire.BY_TYPE)
@EnableSpringConfigured*/
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private UserService userService;

    public void initialize(Unique annotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userService.isEmailAvailable(email);
    }
}
