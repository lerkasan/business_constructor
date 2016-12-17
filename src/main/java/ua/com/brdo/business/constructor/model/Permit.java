package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "legal_document_id")
    private Long legalDocumentId;
    @Column(name = "form_id")
    private Long formId;
    @Column(name = "number")
    private String number;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    @Column(name = "file_example", columnDefinition = "blob")
    private byte[] fileExample;
    @Column(name = "term")
    private String term;
    @Column(name = "propose")
    private String propose;
    @Column(name = "status")
    private Byte status;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permit_type_id", referencedColumnName = "id")
    private PermitType permitType;
}
