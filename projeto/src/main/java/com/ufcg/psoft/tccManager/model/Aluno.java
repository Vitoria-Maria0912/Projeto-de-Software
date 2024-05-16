package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alunos")
public class Aluno {

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

    @JsonProperty("matricula")
    @Column(nullable = false)
    private String matricula;

    @JsonProperty("periodoConclusao")
    @Column(nullable = false)
    private String periodoConclusao;

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
        setPerfil(Perfil.ALUNO);
    }

    @Override
    public String toString(){ return "Aluno : " + this.nomeCompleto;}

}


