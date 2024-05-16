package com.ufcg.psoft.tccManager.service.temaTCC;

import com.ufcg.psoft.tccManager.dto.temaTCC.*;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;

import java.util.*;

public interface TemaTCCService {

    TemaTCCResponseDTO criar(TemaTCCPostPutRequestDTO temaTCCPostPutRequestDTO, UUID id) throws Exception;

    Perfil checarID(UUID id) throws Exception;

    void adicionarTemaTCC(TemaTCC temaTCC, UUID id) throws Exception;

    List<TemaTCCResponseDTO> listarTemasCadastrados(Perfil perfil) throws Exception;

    void enviarNotificacao(String titulo, Iterator<AreaDeEstudo> areasDeEstudoTcc);

    List<TemaTCCResponseDTO> listarTemasCadastradosPeloProfessor(Perfil perfil, UUID id);

    void solicitarOrientacaoTemaTccAluno(UUID idAluno, UUID idProfessor, Long idTema);

    void solicitarOrientacaoTemaProfessor(UUID idAluno, Long idTema, UUID idProfessor);

    void gerarRelatorio(Perfil perfil) throws Exception;
}
