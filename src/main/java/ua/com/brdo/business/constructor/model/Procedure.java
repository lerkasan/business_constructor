package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Entity (name = "procedure")
@Table(name = "procedure_")
@EqualsAndHashCode(of = {"name"})
@JsonInclude(NON_NULL)

public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 1024, unique = true, nullable = false)
    private String name;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "result", length = 2048, nullable = false)
    private String result;

   /* @Column(name = "id_tool", nullable = false) // TODO: where the field?   create a Join
    private Long id_tool;*/

    @Column(name = "cost", nullable = false)
    private String cost;

    @Column(name = "term", nullable = false)
    private String term;

    @Column(name = "method", length = 2048, nullable = false)
    private String method;

    @Column(name = "decision", nullable = false)
    private String decision;

    @Column(name = "deny", nullable = false)
    private String deny;

    @Column(name = "abuse", nullable = false)
    private String abuse;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="procedure_type_id", referencedColumnName="id")
    private ProcedureType procedureType;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="permit_id", referencedColumnName="id")
    private Permit permit;

    @OneToMany(mappedBy = "procedure", cascade = CascadeType.REMOVE)
    private Set<ProcedureDocument> procedureDocuments;

}
