package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "permit")
@Table(name = "permit")
@Data
@EqualsAndHashCode(of = {"name"})
public class Permit {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull(message = "The field name in the permit can't be null")
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "legal_document_id")
    private Long legalDocumentId;
    @Column(name = "form_id")
    private Long formId;
    @NotNull(message = "The field number in the permit can't be null")
    @Column(name = "number")
    private String number;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name = "file_example", columnDefinition = "blob")
    private byte[] fileExample;
    @NotNull(message = "The field term in the permit can't be null")
    @Column(name = "term")
    private String term;
    @NotNull(message = "The field propose in the permit can't be null")
    @Column(name = "propose")
    private String propose;
    @NotNull(message = "The field status in the permit can't be null")
    @Column(name = "status")
    private Byte status;
    @JsonInclude(NON_NULL)
    @NotNull(message = "The field permit type in the permit can't be null")
    @ManyToOne()
    @JoinColumn(name = "permit_type_id", referencedColumnName = "id")
    private PermitType permitType;
}
