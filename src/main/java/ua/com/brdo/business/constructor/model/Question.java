package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EqualsAndHashCode(of = {"text"})
@JsonInclude(NON_NULL)
@Validated
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max=3000, message = "Maximum length of question is 3000 characters.")
    @Column(unique = true, nullable = false, length = 3000)
    private String text;

    @ManyToOne
    @JoinColumn(name="input_type_id", nullable=false)
    private InputType inputType;

    @OneToMany(mappedBy = "question", cascade = ALL)
    @JsonProperty("options")
    private Set<QuestionOption> questionOptions = new HashSet<>();

    public boolean addOption(Option option) {
        Objects.requireNonNull(option);
        QuestionOption questionOption = new QuestionOption();
        questionOption.setQuestion(this);
        questionOption.setOption(option);
        if (option.getQuestionOptions() == null) {
            option.setQuestionOptions(new HashSet<>());
        }
        if (questionOptions == null) {
            this.setQuestionOptions(new HashSet<>());
        }
        boolean result = option.getQuestionOptions().add(questionOption);
        result &= this.getQuestionOptions().add(questionOption);
        return result;
    }

    public boolean deleteOption(Option option) {
        boolean result = false;
        Objects.requireNonNull(option);
        QuestionOption questionOption = new QuestionOption();
        questionOption.setQuestion(this);
        questionOption.setOption(option);
        Set<QuestionOption> questionOptions = option.getQuestionOptions();
        Set<QuestionOption> optionQuestions = this.getQuestionOptions();
        if (questionOptions != null) {
           result = questionOptions.remove(questionOption);
        }
        if (optionQuestions != null) {
            result &= optionQuestions.remove(questionOption);
        }
        return result;
    }
}