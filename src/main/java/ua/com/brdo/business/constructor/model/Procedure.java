package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "name", length = 2048, unique = true, nullable = false)
    private String name;

    @Column(name = "reason", length = 2048, nullable = false)
    private String reason;

    @Column(name = "result", length = 2048, nullable = false)
    private String result;

    @Column(name = "tool_id", nullable = false)
    private Long toolId;

    @Column(name = "cost", length = 2048, nullable = false)
    private String cost;

    @Column(name = "term", length = 4096, nullable = false)
    private String term;

    @Column(name = "method", length = 2048, nullable = false)
    private String method;

    @Column(name = "decision", length = 2048, nullable = false)
    private String decision;

    @Column(name = "deny", length = 3072, nullable = false)
    private String deny;

    @Column(name = "abuse", length = 2048, nullable = false)
    private String abuse;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="procedure_type_id", referencedColumnName="id")
    private ProcedureType procedureType;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="permit_id", referencedColumnName="id")
    private Permit permit;

    @JsonIgnoreProperties(value = {"procedure"})
    @OneToMany(mappedBy = "procedure", cascade = CascadeType.REMOVE)
    private Set<ProcedureDocument> procedureDocuments;

}
