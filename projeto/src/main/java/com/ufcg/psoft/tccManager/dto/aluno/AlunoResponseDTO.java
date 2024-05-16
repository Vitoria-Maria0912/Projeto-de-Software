package com.ufcg.psoft.tccManager.dto.aluno;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponseDTO {

    @JsonProperty("id")
    @Id
    private UUID id;

    @JsonProperty("perfil")
    private Perfil perfil;

    @JsonProperty("nomeCompleto")
    @NotBlank(message = "Nome completo obrigatório")
    private String nomeCompleto;

    @JsonProperty("matricula")
    @NotBlank(message = "Matrícula obrigatória")
    private String matricula;

    @JsonProperty("email")
    @NotBlank(message = "Email obrigatório")
    private String email;

    @JsonProperty("periodoConclusao")
    @NotBlank(message = "Período (previsto) para conclusão obrigatório")
    private String periodoConclusao;

    @Builder.Default
    private Set<AreaDeEstudo> areasDeInteresse = new HashSet<>();

    @JsonProperty("temasDeTcc")
    @Builder.Default
    private Set<TemaTCC> temasTcc = new HashSet<>();

    public AlunoResponseDTO(Aluno aluno) {
        this.id = aluno.getId();
        this.nomeCompleto = aluno.getNomeCompleto();
        this.matricula = aluno.getMatricula();
        this.email = aluno.getEmail();
        this.periodoConclusao = aluno.getPeriodoConclusao();
        this.perfil = aluno.getPerfil();
        this.areasDeInteresse = aluno.getAreasDeInteresse();
        this.temasTcc = aluno.getTemasTcc();
    }
}