package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.brdo.business.constructor.constraint.Unique;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "questionnaire")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "businessType.codeKved"})
@Validated
@JsonInclude(NON_NULL)
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "Title field of questionnaire is required.")
    @Size(max = 1000, message = "Maximum length of questionnaire title is 1000 characters.")
    @Column(unique = true, nullable = false, length = 1000)
    @Unique(object = Questionnaire.class, field = "title", message = "Questionnaire with specified title already exists in database. Title should be unique.")
    private String title;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="business_type_id", referencedColumnName="id")
    @NotNull(message = "Business type field is required.")
    private BusinessType businessType;

    @Valid
    @OneToMany(mappedBy = "questionnaire", cascade = REMOVE)
    @JsonIgnoreProperties(value = {"questionnaire"})
    @OrderBy(value = "id ASC")
    private Set<Question> questions = new HashSet<>();

    public boolean addQuestion(Question question) {
        Objects.requireNonNull(question);
        if (questions == null) {
            questions = new HashSet<>();
        }
        if (questions.contains(question)) {
            throw new IllegalArgumentException("Question with the same text already exists in this questionnaire.");
        }
        return questions.add(question);
    }

    public boolean deleteQuestion(Question question) {
        Objects.requireNonNull(question);
        return questions == null || questions.remove(question);
    }
}
