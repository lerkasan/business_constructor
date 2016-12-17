package ua.com.brdo.business.constructor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "legalDocument")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class LegalDocument {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @NotBlank(message = "Legal Document idRada must not be blank!")
    @Size(max=24, message = "Legal Document idRada must be shorter than 50 symbols!")
    @Column(name = "idRada", length = 50, nullable = false)
    private String idRada;
    @NotBlank(message = "Legal Document idLiga must not be blank!")
    @Size(max=24, message = "Legal Document idLiga must be shorter than 24 symbols!")
    @Column(name = "idLiga", length = 24, nullable = false)
    private String idLiga;

    @Column(name = "idState", nullable = false)
    private int idState;

    @Column(name = "datePub", nullable = false)
    private int datePub;

    @Column(name = "dateAdd", nullable = false)
    private int dateAdd;
    @NotBlank(message = "Legal Document numberPub must not be blank!")
    @Column(name = "numberPub", nullable = false)
    private String numberPub;
    @NotBlank(message = "Legal Document title must not be blank!")
    @Size(max=24, message = "Legal Document title must be shorter than 1024 symbols!")
    @Column(name = "title", length =1024, nullable = false)
    private String title;
    @NotBlank(message = "Legal Document numberRada must not be blank!")
    @Column(name = "numberRada", nullable = false)
    private String numberRada;
    @NotBlank(message = "Legal Document numberMj must not be blank!")
    @Column(name = "numberMj", nullable = false)
    private String numberMj;

    @Column(name = "inRada", nullable = false)
    private byte inRada;

    @Column(name = "inLiga", nullable = false)
    private byte inLiga;

    @Column(name = "inBrdo", nullable = false)
    private byte inBrdo;

    @Column(name = "autoLiga", nullable = false)
    private byte autoLiga;

    @Column(name = "autoBrdo", nullable = false)
    private byte autoBrdo;

    @Column(name = "regulation", nullable = false)
    private int regulation;
    @NotBlank(message = "Legal Document manualSector must not be blank!")
    @Size(max=24, message = "Legal Document manualSector must be shorter than 96 symbols!")
    @Column(name = "manualSector", length = 96, nullable = false)
    private String manualSector;

    @Column(name = "techRegulation", nullable = false)
    private int techRegulation;
}
