package ua.com.brdo.business.constructor.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stage")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"answer"})
@JsonInclude(NON_NULL)
public class Stage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy="stage", cascade = REMOVE)
    private Answer answer;

    private int priority;

    private boolean finished;

    @JsonProperty("procedure")
    public Procedure getProcedure() {
        return answer.getProcedure();
    }
}
