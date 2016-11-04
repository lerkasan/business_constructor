package ua.com.brdo.business.constructor.model.entity;

import ua.com.brdo.business.constructor.model.dto.UserDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String passwordHash;
    private LocalDateTime creationTimestamp;
    private boolean isNotificationEnabled;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        creationTimestamp = LocalDateTime.now();
        isNotificationEnabled = true;
    }

    public User(UserDto userDto) {
        username = userDto.getUsername();
        firstName = userDto.getFirstName();
        middleName = userDto.getMiddleName();
        lastName = userDto.getLastName();
        email = userDto.getEmail();
        creationTimestamp = LocalDateTime.now();
        isNotificationEnabled = userDto.isNotificationEnabled();
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreationTimestamp() {
        return LocalDateTime.of(creationTimestamp.getYear(),creationTimestamp.getMonth(),creationTimestamp.getDayOfMonth(),
                                creationTimestamp.getHour(),creationTimestamp.getMinute(),creationTimestamp.getSecond());
    }

    public void setCreationTimestamp(LocalDateTime creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public boolean isNotificationEnabled() {
        return isNotificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        isNotificationEnabled = notificationEnabled;
    }

    public Set<Role> getRoles() {
        return new HashSet<>(roles);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
