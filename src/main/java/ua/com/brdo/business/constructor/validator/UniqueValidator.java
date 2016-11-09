package ua.com.brdo.business.constructor.validator;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.service.UserService;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    UserService userService;

    public void initialize(Unique annotation) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (userService.findByEmail(email) != null) {
            return false;
        }
        return true;
    }
}
