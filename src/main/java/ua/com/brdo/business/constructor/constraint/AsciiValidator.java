package ua.com.brdo.business.constructor.constraint;

import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.SneakyThrows;

@Component
public class AsciiValidator implements ConstraintValidator<Ascii, char[]> {

    private static final char MIN_ALLOWED_CHAR_SPACE = 32;
    private static final char MAX_ALLOWED_CHAR_TILDE = 126;

    @Override
    public void initialize(Ascii annotation) {
    }

    @SneakyThrows
    @Override
    public boolean isValid(char[] param, ConstraintValidatorContext context) {
        if (param == null) {
            return false;
        }
        for (int index = 0; index < param.length; index++) {
            if ((param[index] < MIN_ALLOWED_CHAR_SPACE) || (param[index] > MAX_ALLOWED_CHAR_TILDE)) {
                return false;
            }
        }
        return true;
    }
}