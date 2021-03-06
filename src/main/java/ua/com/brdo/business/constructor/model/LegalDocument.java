package ua.com.brdo.business.constructor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    private Long id;
    @NotNull(message = "Legal Document idRada must not be null!")
    @Size(max=50, message = "Legal Document idRada must be shorter than 50 symbols!")
    @Column(name = "idRada", length = 50, nullable = false)
    private String idRada;
    @NotNull(message = "Legal Document idLiga must not be null!")
    @Size(max=24, message = "Legal Document idLiga must be shorter than 24 symbols!")
    @Column(name = "idLiga", length = 24, nullable = false)
    private String idLiga;
    @Column(name = "idState", nullable = false)
    private Integer idState;
    @Column(name = "datePub", nullable = false)
    private Integer datePub;
    @Column(name = "dateAdd", nullable = false)
    private Integer dateAdd;
    @NotNull(message = "Legal Document numberPub must not be null!")
    @Size(max=255, message = "Legal Document numberPub must be shorter than 50 symbols!")
    @Column(name = "numberPub", nullable = false)
    private String numberPub;
    @NotNull(message = "Legal Document title must not be null!")
    @Size(max=1024, message = "Legal Document title must be shorter than 1024 symbols!")
    @Column(name = "title", length =1024, nullable = false)
    private String title;
    @NotNull(message = "Legal Document numberRada must not be null!")
    @Size(max=255, message = "Legal Document numberRada must be shorter than 255 symbols!")
    @Column(name = "numberRada", nullable = false)
    private String numberRada;
    @NotNull(message = "Legal Document numberMj must not be null!")
    @Size(max=64, message = "Legal Document numberMj must be shorter than 64 symbols!")
    @Column(name = "numberMj", nullable = false)
    private String numberMj;
    @Column(name = "inRada", nullable = false)
    private Byte inRada;
    @Column(name = "inLiga", nullable = false)
    private Byte inLiga;
    @Column(name = "inBrdo", nullable = false)
    private Byte inBrdo;
    @Column(name = "autoLiga", nullable = false)
    private Byte autoLiga;
    @Column(name = "autoBrdo", nullable = false)
    private Byte autoBrdo;
    @Column(name = "regulation", nullable = false)
    private Integer regulation;
    @NotNull(message = "Legal Document manualSector must not be null!")
    @Size(max=96, message = "Legal Document manualSector must be shorter than 96 symbols!")
    @Column(name = "manualSector", length = 96, nullable = false)
    private String manualSector;
    @Column(name = "techRegulation", nullable = false)
    private Integer techRegulation;
}
