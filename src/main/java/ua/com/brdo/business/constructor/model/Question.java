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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@EqualsAndHashCode(of = {"id"})
@JsonInclude(NON_NULL)
@Validated
public class Question {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotEmpty(message = "Text field in question is required.")
    @Size(max=1000, message = "Maximum length of question is 1000 characters.")
    @Column(nullable = false, length = 1000)
    private String text;

    @JsonProperty("input_type")
    @Enumerated(EnumType.STRING)
    @Column(name="input_type", nullable = false, columnDefinition = "VARCHAR(255) DEFAULT \"SINGLE_CHOICE\"")
    private InputType inputType = InputType.SINGLE_CHOICE;

    @OneToMany(mappedBy = "question", cascade = ALL)
    private Set<Option> options = new HashSet<>();

    public String getInputType() {
        if (inputType == null) {
            inputType = InputType.SINGLE_CHOICE;
        }
        return inputType.name();
    }

    public void setInputType(String inputType) {
        this.inputType = InputType.valueOf(inputType);
    }

    public boolean addOption(Option option) {
        Objects.requireNonNull(option);
        if (options == null) {
            options = new HashSet<>();
        }
        return options.add(option);
    }

    public boolean deleteOption(Option option) {
        Objects.requireNonNull(option);
        return options == null || options.remove(option);
    }
}