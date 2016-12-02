package ua.com.brdo.business.constructor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "legal_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class LegalDocument {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "id_rada", length = 50, nullable = false)
    private String idRada;
    @Column(name = "id_liga", length = 24, nullable = false)
    private String idLiga;
    @Column(name = "id_state", nullable = false)
    private int idState;
    @Column(name = "date_pub", nullable = false)
    private int datePub;
    @Column(name = "date_add", nullable = false)
    private int dateAdd;
    @Column(name = "number_pub", nullable = false)
    private String numberPub;
    @Column(name = "title", length =1024, nullable = false)
    private String title;
    @Column(name = "number_rada", nullable = false)
    private String numberRada;
    @Column(name = "number_mj", nullable = false)
    private String numberMj;
    @Column(name = "in_rada", nullable = false)
    private byte inRada;
    @Column(name = "in_liga", nullable = false)
    private byte inLiga;
    @Column(name = "in_brdo", nullable = false)
    private byte inBrdo;
    @Column(name = "auto_liga", nullable = false)
    private byte autoLiga;
    @Column(name = "auto_brdo", nullable = false)
    private byte autoBrdo;
    @Column(name = "regulation", nullable = false)
    private int regulation;
    @Column(name = "manual_sector", length = 96, nullable = false)
    private String manualSector;
    @Column(name = "tech_regulation", nullable = false)
    private int techRegulation;
}
