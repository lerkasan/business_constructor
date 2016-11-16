package ua.com.brdo.business.constructor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"role"})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 100, unique = true, nullable = false)
    private Long id;
    @Column(name = "role", length = 100, unique = true, nullable = false)
    private String role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    Set<User> users;

    @Override
    public String getAuthority() {
        return getRole();
    }

}
