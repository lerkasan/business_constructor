package ua.com.brdo.business.constructor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"title"})
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", length = 100, unique = true, nullable = false)
    private Long id;
    @Column(name = "title", length = 100, unique = true, nullable = false)
    private String title;

    @Override
    public String getAuthority() {
        return getTitle();
    }

}
