package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professores")
public class Professor {

    @JsonProperty("id")
    @Id
    @GeneratedValue
    private UUID id;

    @JsonProperty("perfil")
    @Column(nullable = true)
    private Perfil perfil;

    @JsonProperty("nomeCompleto")
    @Column(nullable = false)
    private String nomeCompleto;

    @JsonProperty("email")
    @Column(nullable = false)
    private String email;

    @JsonProperty("laboratorios")
    @Column(nullable = false)
    private HashSet<String> laboratorios;

    @JsonProperty("quota")
    @Column(nullable = false)
    @Min(value = 0, message = "A quota deve ser positiva!")
    private Integer quota;

    @JsonProperty("areasDeInteresse")
    @ManyToMany
    @Builder.Default
    private Set<AreaDeEstudo> areasDeInteresse = new HashSet<>();

    @JsonProperty("temasDeTcc")
    @ManyToMany
    @Builder.Default
    private Set<TemaTCC> temasTcc = new HashSet<>();

    @PrePersist
    private void setPerfil() {
        setPerfil(Perfil.PROFESSOR);
    }

    @Override
    public String toString(){ return "Professor : " + this.nomeCompleto;}
}
