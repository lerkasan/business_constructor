package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"text"})
@JsonInclude(NON_NULL)
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonIgnoreProperties(value = {"children"}) */
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 3000)
    private String text;

    @ManyToOne
    @JoinColumn(name="input_type_id", nullable=false)
    private InputType inputType;

    @OneToMany(mappedBy = "question")
    private Set<Option> options;



    /*@ManyToMany
    @JoinTable(name = "question_question",
            joinColumns = {@JoinColumn(name = "previous_id")},
            inverseJoinColumns = @JoinColumn(name = "next_id"))
    private Set<Question> previousQuestions;

    @ManyToMany(mappedBy = "previousQuestions")
    private Set<Question> nextQuestions;
*/
//    public Set<Option> getOptions() {
//        return Collections.unmodifiableSet(options);
//    }

    /*
    @ManyToOne
    @JoinColumn(name="parent_id")
    @JsonBackReference(value = "children")
    private Question parent;

    @JsonIgnore
    @OneToMany(mappedBy="parent")
    @JsonManagedReference(value = "parent")
    private Set<Question> children;
    */
}