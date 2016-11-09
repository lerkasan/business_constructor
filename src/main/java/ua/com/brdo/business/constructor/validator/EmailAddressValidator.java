package ua.com.brdo.business.constructor.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ua.com.brdo.business.constructor.constraint.EmailAddress;

public class EmailAddressValidator implements ConstraintValidator<EmailAddress, String> {

    public void initialize(EmailAddress annotation) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}
