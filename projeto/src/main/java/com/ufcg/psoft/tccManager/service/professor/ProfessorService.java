package com.ufcg.psoft.tccManager.service.professor;

import com.ufcg.psoft.tccManager.dto.professor.*;
import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import com.ufcg.psoft.tccManager.model.Professor;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;
import jakarta.transaction.Transactional;

import java.util.*;

public interface ProfessorService {

    ProfessorResponseDTO criar(ProfessorPostPutRequestDTO professorPostPutRequestDTO, Perfil perfil);

    ProfessorResponseDTO recuperar(UUID id, Perfil perfil);

    List<ProfessorResponseDTO> listar(Perfil perfil);

    ProfessorResponseDTO alterarProfessor(UUID id, ProfessorPostPutRequestDTO professorPostPutRequestDTO, Perfil perfil);

    void removerProfessor(UUID id, Perfil perfil);

    Set<ProfessorResponseNomeEmailDTO> listarProfessoresDisponiveis(Set<AreaDeEstudo> areasDoAluno);

    ProfessorResponseDTO atualizarQuota(UUID idProfessor, Integer novaQuota, Perfil perfil);

    void diminuiQuotaEmUm(UUID idProfessor);

    void informarAreaDeInteresseProf(UUID id, Long idArea);
    @Transactional
    void receberNotificacao(String tituloTema, String nomeAluno, String emailAluno, Professor professor);
}
