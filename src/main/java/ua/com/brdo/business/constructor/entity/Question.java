package ua.com.brdo.business.constructor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"text"})
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 3000)
    private String text;

    @ManyToOne
    @JoinColumn(name="parent_id")
    //@JsonIgnore
    private Question parent;

    @OneToMany(mappedBy="parent")
    //@JoinColumn(name="child_id")
    @JsonIgnore
    private Set<Question> children;

    @ManyToOne
    @JoinColumn(name="input_type_id", nullable=false)
    InputType inputType;
}