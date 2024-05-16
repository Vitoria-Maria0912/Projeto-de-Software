package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SolicitacaoTCC")
public class Solicitacao {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("aluno")
    @OneToOne
    private Aluno aluno;

    @JsonProperty("professor")
    @OneToOne
    private Professor professor;

    @JsonProperty("temaTcc")
    @OneToOne
    private TemaTCC temaTcc;

}
