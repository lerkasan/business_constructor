package ua.com.brdo.business.constructor.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.persistence.*;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Entity (name = "procedure")
@Table(name = "procedure")
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

    @Column(name="permit_id", nullable = false)
    private Long permitId;

    @Column(name="id_type", nullable = false)
    private Long idType;

   /* @Column(name = "id_tool", nullable = false) // where the field?   create a Join
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", referencedColumnName = "id", insertable = false, updatable = false)
    private ProcedureType procedureType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permit_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Permit permit;

    @JsonIgnore
    @OneToMany(mappedBy = "procedure", cascade = CascadeType.REMOVE)
    private Set<ProcedureDocument> procedureDocuments;

}
