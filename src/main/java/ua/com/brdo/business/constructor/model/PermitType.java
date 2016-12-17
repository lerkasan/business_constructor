package ua.com.brdo.business.constructor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "permit_type")
@Data
@EqualsAndHashCode(of = {"name"})
@JsonIgnoreProperties(value = {"permits"})
public class PermitType {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", unique = true)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "permitType", cascade = CascadeType.REMOVE)
    private Set<Permit> permits;
}
