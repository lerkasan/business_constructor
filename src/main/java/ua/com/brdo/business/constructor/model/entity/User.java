package ua.com.brdo.business.constructor.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.com.brdo.business.constructor.model.dto.UserDto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(of = {"username", "email", "creationDate"})
@JsonInclude(NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    @JsonFormat(pattern = "dd-MM-yyyy")
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
        Objects.requireNonNull(userDto);
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
        return newUser;
    }

    public static User of(UserDto userDto, Role role) {
        Objects.requireNonNull(userDto);
        Objects.requireNonNull(role);
        User newUser = User.of(userDto);
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
}
