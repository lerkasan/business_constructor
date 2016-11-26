package ua.com.brdo.business.constructor.constraint.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.service.UserService;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private UserService userService;

    private String type = "";

    public void initialize(Unique annotation) {
        switch (annotation.type()) {
            case "email":
                type = "email";
                break;
            case "username":
                type = "username";
                break;
            default:
                throw new IllegalArgumentException("Unexpected type passed to Unique annotation.");
        }
    }

    private boolean validateParam(String param) {
        boolean valid = false;

        if ("email".equals(type)) {
            valid = userService.isEmailAvailable(param);
        } else if ("username".equals(type)) {
            valid =  userService.isUsernameAvailable(param);
        }
        return valid;
    }

    @SneakyThrows
    @Override
    public boolean isValid(String param, ConstraintValidatorContext context) {
        return (userService == null) || (param == null) || validateParam(param);
    }
}
