package ua.com.brdo.business.constructor.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import ua.com.brdo.business.constructor.constraint.Unique;

@Entity
@Table(name = "business_type")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "codeKved"})
@JsonInclude(NON_NULL)
@Validated
@Unique.List(value = {
    @Unique(field = "title", message = "Business type with specified title already exists in database. Title should be unique."),
    @Unique(field = "codeKved", message = "Business type with specified KVED code already exists in database. KVED code should be unique.")
})
public class BusinessType {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "Title field is required.")
    @Size(max = 1000, message = "Maximum length of title is 1000 characters.")
    @Column(unique = true, nullable = false, length = 1000)
    // @Unique(object = BusinessType.class, field = "title", message = "Business type with specified title already exists in database. Title should be unique.")
    private String title;

    @NotEmpty(message = "KVED code field is required.")
    @Pattern(regexp = "\\d{2}+\\.+\\d{2}", message = "Format of KVED code must be a pair of two-digit numbers separated by dot. Example: 62.21")
    @Column(unique = true, nullable = false, length = 5, name = "code_kved")
    // @Unique(object = BusinessType.class, field = "codeKved", message = "Business type with specified KVED code already exists in database. KVED code should be unique.")
    private String codeKved;
}
