package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.brdo.business.constructor.service.QuestionService;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "option_")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"title", "question"})
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(value = {"question"})
public class Option {

    @Autowired
    @Transient
    @JsonIgnore
    QuestionService questionService;

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
    @PrimaryKeyJoinColumn(name="next_question", referencedColumnName="id")
    @JsonIgnore
    // @JsonIgnoreProperties({"text", "input_type", "options" })
    // @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    // @JsonIdentityReference(alwaysAsId=true)
    private Question nextQuestion;

    // private Procedure procedure; TODO - uncomment in further tasks

    public Option(String title) {
        this.title = title;
    }

    @JsonProperty("next_question")
    public Long getNextQuestionId() {
        if (nextQuestion == null) {
            return  null;
        }
        return nextQuestion.getId();
    }

    @JsonProperty("next_question")
    public void setNextQuestionId(Long nextQuestionId) {
        if (nextQuestionId != null) {
            nextQuestion = questionService.findById(nextQuestionId);
        }
    }
}
