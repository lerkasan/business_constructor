package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "option_")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "question"})
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(value = {"question"})
public class Option {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "Title field of option is required.")
    @Size(max=500, message = "Maximum length of option is 500 characters.")
    @Column(nullable = false, length = 500)
    private String title;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="question_id", referencedColumnName="id")
    private Question question;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="next_question_id", referencedColumnName="id")
    @JsonIgnoreProperties(value = {"options", "inputType", "text"})
    private Question nextQuestion;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="procedure_id", referencedColumnName="id")
    @JsonIgnoreProperties(value = {"decision", "reason", "result", "cost", "term", "method",
            "deny", "abuse", "procedureType", "permit", "procedureDocuments"})
    private Procedure procedure;

    public void setNextQuestion(Question nextQuestion) {
        checkLinkBetweenQuestionAndNextQuestion();
        this.nextQuestion = nextQuestion;
    }

    public void checkLinkBetweenQuestionAndNextQuestion() {
        if ((nextQuestion != null) && (question != null) && (nextQuestion.getId().equals(question.getId()))) {
            throw new IllegalArgumentException("Question can't be linked to itself.");
        }
    }
}
