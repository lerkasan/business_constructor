package ua.com.brdo.business.constructor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.com.brdo.business.constructor.constraint.EmailAddress;
import ua.com.brdo.business.constructor.constraint.Unique;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
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

    @NotEmpty(message = "E-mail field is required.")
    @EmailAddress(message = "Incorrect format of e-mail.")
    @Unique(message = "User with this e-mail is already registered.")
    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = WRITE_ONLY)
    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, max = 32, message = "Password length must be between 8 and 100 characters.")
    @Pattern(regexp = "^[!-~]{8,32}$", message = "Password could include upper and lower case latin letters, numerals (0-9) and special symbols.")
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate = LocalDate.now();

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
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
