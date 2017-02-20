package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "business")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"creationDate", "businessType.id"})
@JsonInclude(NON_NULL)
public class Business {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "business_type_id", referencedColumnName = "id")
    @NotNull
    private BusinessType businessType;

    @Size(max = 1000, message = "Maximum length of title is 1000 characters.")
    @Column(nullable = false, length = 1000)
    private String title;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"username", "email", "creationDate", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled", "roles"})
    private User user;

    @Column(nullable = false, updatable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate creationDate = LocalDate.now();
}
