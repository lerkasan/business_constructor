package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name = "example_file", columnDefinition = "blob")
    private byte[] exampleFile;

    @ManyToOne
    @PrimaryKeyJoinColumn(name="procedure_id", referencedColumnName="id")
    private Procedure procedure;
}
