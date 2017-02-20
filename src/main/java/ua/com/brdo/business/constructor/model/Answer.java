package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"business", "option"})
@JsonInclude(NON_NULL)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "business_id", referencedColumnName = "id")
    @NotNull
    @JsonIgnoreProperties(value = {"businessType.title", "businessType.codeKved", "title", "creationDate"})
    private Business business;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "option_id", referencedColumnName = "id")
    @NotNull
    @JsonIgnoreProperties(value = {"title", "nextQuestion", "procedure", "question"})
    private Option option;

    @JsonIgnore
    public Question getQuestion() {
        return option.getQuestion();
    }
}
