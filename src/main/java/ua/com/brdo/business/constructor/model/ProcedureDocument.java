package ua.com.brdo.business.constructor.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Entity(name = "procedure_document")
@Table(name = "procedure_document")
@EqualsAndHashCode(of = {"name"})
@JsonInclude(NON_NULL)

public class ProcedureDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "procedure_id")
    private Long procedureId;

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name = "example_file", columnDefinition = "blob")
    private byte[] exampleFile;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_id", nullable = false, insertable = false, updatable = false)
    private Procedure procedure;


}
