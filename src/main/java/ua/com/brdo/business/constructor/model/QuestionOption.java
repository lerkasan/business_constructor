package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "question_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"question", "option"})
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(value = {"question", "id"})
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    // private Question nextQuestion; TODO - uncomment in further tasks

    // private Procedure procedure; TODO - uncomment in further tasks
}
