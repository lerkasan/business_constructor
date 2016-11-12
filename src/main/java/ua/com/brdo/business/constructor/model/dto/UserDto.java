package ua.com.brdo.business.constructor.model.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"username", "email", "creationDate"})
public final class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirm;
    private LocalDate creationDate;

}
