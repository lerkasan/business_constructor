package ua.com.brdo.business.constructor.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    public static User of(UserDto userDto) {
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
        newUser.setRoles(new HashSet<>());
        return newUser;
    }

    public User(UserDto userDto) {
        email = userDto.getEmail();
        if (userDto.getUsername() != null) {
            username = userDto.getUsername();
        } else {
            username = email;
        }
        firstName = userDto.getFirstName();
        middleName = userDto.getMiddleName();
        lastName = userDto.getLastName();
        roles = new HashSet<>();
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }


}
