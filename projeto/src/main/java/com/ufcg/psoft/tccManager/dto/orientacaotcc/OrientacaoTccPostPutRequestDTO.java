package com.ufcg.psoft.tccManager.dto.orientacaotcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrientacaoTccPostPutRequestDTO {

    @JsonProperty("aluno")
    @NotBlank(message = "Aluno obrigatório")
    private Aluno aluno;

    @JsonProperty("professor")
    @NotBlank(message = "Professor obrigatório")
    private Professor professor;

    @JsonProperty("temaTcc")
    @NotBlank(message = "Tema do TCC obrigatório")
    private TemaTCC temaTCC;
}
