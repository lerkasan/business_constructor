package ua.com.brdo.business.constructor.model.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import ua.com.brdo.business.constructor.annotation.Unique;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


public final class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;

    @NotEmpty
    @Email
    @Unique
    private String email;

    @NotEmpty
    @Size(min = 8, max = 100)
    @Pattern(regexp = "^[!-~]{8,100}$")
    private String password;

    @NotEmpty
    @Size(min = 8, max = 100)
    @Pattern(regexp = "^[!-~]{8,100}$")
    private String passwordConfirm;

    private LocalDateTime creationTimestamp;
    private boolean isNotificationEnabled;

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }

    public LocalDateTime getCreationTimestamp() {
        return LocalDateTime.of(creationTimestamp.getYear(),creationTimestamp.getMonth(),creationTimestamp.getDayOfMonth(),
                creationTimestamp.getHour(),creationTimestamp.getMinute(),creationTimestamp.getSecond());
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public boolean checkPasswordsMatch() {
        if ((password != null) && (passwordConfirm != null) && (password.equals(passwordConfirm))) {
            return true;
        }
        return false;
    }
}
