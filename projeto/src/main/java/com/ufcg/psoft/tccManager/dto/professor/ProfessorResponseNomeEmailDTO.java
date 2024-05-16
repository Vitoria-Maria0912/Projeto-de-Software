package com.ufcg.psoft.tccManager.dto.professor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.Professor;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponseNomeEmailDTO {

    @JsonProperty("nomeCompleto")
    @NotBlank(message = "Nome completo obrigatorio!")
    private String nomeCompleto;

    @JsonProperty("email")
    @NotBlank(message = "Email obrigatorio!")
    private String email;

    public ProfessorResponseNomeEmailDTO(Professor professor) {
        this.nomeCompleto = professor.getNomeCompleto();
        this.email = professor.getEmail();
    }
}
