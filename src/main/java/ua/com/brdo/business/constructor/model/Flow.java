package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "flow")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"title", "codeKved"})
@JsonInclude(NON_NULL)
public class Flow {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "business_id", referencedColumnName = "id")
    @NotNull
    private Business business;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "procedure_id", referencedColumnName = "id")
    @NotNull
    private Procedure procedure;

    private int priority;

    private boolean finished;
}
