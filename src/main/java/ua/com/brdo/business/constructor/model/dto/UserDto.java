package ua.com.brdo.business.constructor.model.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;

import java.time.LocalDate;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.brdo.business.constructor.constraint.Unique;

@NoArgsConstructor
@Data
@ScriptAssert(lang = "javascript", script = "_this.password == _this.passwordConfirm", message = "Password and password confirmation don't match.")
public final class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;

    @NotEmpty(message = "Email field is required.")
    @Email(message = "Incorrect format of e-mail.")
    //@EmailAddress(message = "E-mail format is incorrect.")
    @Unique(message = "User with this e-mail is already registered.")
    private String email;

    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 100, message = "Password length must be between 8 and 100 characters.")
    @Pattern(regexp = "^[!-~]{0,100}$", message = "Password could include upper and lower case latin letters, numerals (0-9) and special symbols.")
    private String password;

    @NotEmpty(message = "Password confirmation field is required.")
    // @Size(min = 8, max = 100, message = "Password length mush be between 8 and 100 characters.")
    // @Pattern(regexp = "^[!-~]{0,100}$", message = "Password could include upper and lower case latin letters, numerals (0-9) and special symbols.")
    private String passwordConfirm;
    private LocalDate creationDate;
    private boolean isNotificationEnabled;

}
