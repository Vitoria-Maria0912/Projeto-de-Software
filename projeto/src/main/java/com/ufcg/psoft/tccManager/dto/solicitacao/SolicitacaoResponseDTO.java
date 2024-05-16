package com.ufcg.psoft.tccManager.dto.solicitacao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class SolicitacaoResponseDTO {

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

    public SolicitacaoResponseDTO(Solicitacao solicitacao){
        this.id = solicitacao.getId();
        this.aluno = solicitacao.getAluno();
        this.professor = solicitacao.getProfessor();
        this.temaTCC = solicitacao.getTemaTcc();
    }
}

