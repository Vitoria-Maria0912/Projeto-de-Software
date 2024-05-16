package com.ufcg.psoft.tccManager.dto.orientacaotcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class OrientacaoTccResponseDTO {

    @JsonProperty("id")
    @Id
    private Long id;

    @JsonProperty("aluno")
    @NotBlank(message = "Aluno obrigatório")
    private Aluno aluno;

    @JsonProperty("professor")
    @NotBlank(message = "Professor obrigatório")
    private Professor professor;

    @JsonProperty("temaTcc")
    @NotBlank(message = "Tema do TCC obrigatório")
    private TemaTCC temaTCC;

    public OrientacaoTccResponseDTO(OrientacaoTcc orientacaoTcc){
        this.id = orientacaoTcc.getId();
        this.aluno = orientacaoTcc.getAluno();
        this.professor = orientacaoTcc.getProfessor();
        this.temaTCC = orientacaoTcc.getTemaTcc();
    }
}
