package ua.com.brdo.business.constructor.constraint;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;

@Component
public class AsciiValidator implements ConstraintValidator<Ascii, char[]> {

    public void initialize(Ascii annotation) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(char[] param, ConstraintValidatorContext context) {
        if (param == null) {
            return false;
        }
        for (int index = 0; index < param.length; index++) {
            if ((param[index] < 32) || (param[index] > 126)) {
                return false;
            }
        }
        return true;
    }
}