package ua.com.brdo.business.constructor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.com.brdo.business.constructor.model.dto.UserDto;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(of = {"username", "email", "creationDate"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate = LocalDate.now();

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
    }

    public static User of(UserDto userDto, Role role) {
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        if (userDto.getUsername() != null) {
            newUser.setUsername(userDto.getUsername());
        } else {
            newUser.setUsername(userDto.getEmail());
        }
        newUser.setFirstName(userDto.getFirstName());
        newUser.setMiddleName(userDto.getMiddleName());
        newUser.setLastName(userDto.getLastName());
        newUser.setPasswordHash(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        newUser.grantRole(role);
        return newUser;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean grantRole(Role role) {
        return roles.add(role);
    }

    public boolean revokeRole(Role role) {
        return roles.remove(role);
    }

    public String encodePassword(String password) {
        passwordHash = new BCryptPasswordEncoder().encode(password);
        return passwordHash;
    }
}
