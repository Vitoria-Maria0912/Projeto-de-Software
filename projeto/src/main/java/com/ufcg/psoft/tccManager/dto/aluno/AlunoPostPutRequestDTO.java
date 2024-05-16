package com.ufcg.psoft.tccManager.dto.aluno;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoPostPutRequestDTO {

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

    @JsonProperty("areasDeInteresse")
    private Set<AreaDeEstudo> areasDeInteresse;

    @JsonProperty("temasDeTcc")
    @Builder.Default
    private HashSet<TemaTCC> temasTcc = new HashSet<>();
}