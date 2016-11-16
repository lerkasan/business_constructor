package ua.com.brdo.business.constructor.constraint.validator;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private UserService userService;

    private String type = "";

    public void initialize(Unique annotation) {
        switch (annotation.type()) {
            case "email": {
                type = "email";
                break;
            }
            case "username": {
                type = "username";
                break;
            }
        }
    }

    @SneakyThrows
    @Override
    public boolean isValid(String param, ConstraintValidatorContext context) {
        if ((userService == null) || (param == null) ) {
            return true;
        }
        if ("email".equals(type)) {
            return userService.isEmailAvailable(param);
        }
        if ("username".equals(type)) {
            return userService.isUsernameAvailable(param);
        }
        return false;
    }
}
