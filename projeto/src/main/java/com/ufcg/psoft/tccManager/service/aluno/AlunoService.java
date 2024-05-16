package com.ufcg.psoft.tccManager.service.aluno;

import com.ufcg.psoft.tccManager.dto.aluno.*;
import com.ufcg.psoft.tccManager.dto.professor.ProfessorResponseNomeEmailDTO;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.validation.Valid;

import java.util.*;

public interface AlunoService {
    
    AlunoResponseDTO criar(AlunoPostPutRequestDTO alunoPostPutRequestDTO, Perfil perfil);

    AlunoResponseDTO recuperar(UUID id, Perfil perfil);

    List<AlunoResponseDTO> listar(Perfil perfil);

    AlunoResponseDTO atualizaAluno(@Valid UUID id, AlunoPostPutRequestDTO alunoPostPutRequestDTO, Perfil perfil);

    void removerAluno(UUID id, Perfil perfil);

    void printarNotificacao(String titulo, AlunoResponseDTO aluno, AreaDeEstudo areaDeEstudo);

    void receberNotificacao(String titulo, AreaDeEstudo areaDeEstudo);

    void informarAreaDeInteresse (UUID idAluno, Long idArea);

    Set<ProfessorResponseNomeEmailDTO> listarProfessoresDisponiveis(UUID id);
}