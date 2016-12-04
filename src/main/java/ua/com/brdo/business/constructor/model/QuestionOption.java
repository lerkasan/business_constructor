package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "question_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"question", "option"})
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(value = {"question", "id"})
//@NamedQuery(name = "QuestionOption.removeByQuestionId", query = "DELETE FROM question_option q WHERE q.question_id = :questionId")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="question_id", referencedColumnName="id")
    private Question question;

    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH})
    @PrimaryKeyJoinColumn(name="option_id", referencedColumnName="id")
    private Option option;

    // private Question nextQuestion; TODO - uncomment in further tasks

    // private Procedure procedure; TODO - uncomment in further tasks
}
