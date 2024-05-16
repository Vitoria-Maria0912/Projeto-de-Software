package com.ufcg.psoft.tccManager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orientacaoTCC")
public class OrientacaoTcc {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("aluno")
    @OneToOne(cascade = CascadeType.ALL)
    private Aluno aluno;

    @JsonProperty("professor")
    @OneToOne(cascade = CascadeType.ALL)
    private Professor professor;

    @JsonProperty("temaTcc")
    @OneToOne(cascade = CascadeType.ALL)
    private TemaTCC temaTcc;

}
