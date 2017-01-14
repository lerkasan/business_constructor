package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Validated
@JsonInclude(NON_NULL)
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "Text field of question is required.")
    @Size(max=1000, message = "Maximum length of question is 1000 characters.")
    @Column(nullable = false, length = 1000)
    private String text;

    @NotNull(message = "Input type is required.")
    @Enumerated(EnumType.STRING)
    @Column(name="input_type", nullable = false)
    private InputType inputType;

    @Valid
    @OneToMany(mappedBy = "question", cascade = ALL)
    private List<Option> options = new ArrayList<>();

    @ManyToOne
    @PrimaryKeyJoinColumn(name="questionnaire_id", referencedColumnName="id")
    @JsonIgnoreProperties(value = {"questions"})
    private Questionnaire questionnaire;

    public boolean addOption(Option option) {
        Objects.requireNonNull(option);
        if (options == null) {
            options = new ArrayList<>();
        }
        return options.add(option);
    }

    public boolean deleteOption(Option option) {
        Objects.requireNonNull(option);
        return options == null || options.remove(option);
    }
}
