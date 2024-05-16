package com.ufcg.psoft.tccManager.service.Solicitacao;

import com.ufcg.psoft.tccManager.dto.solicitacao.SolicitacaoResponseDTO;
import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.model.enumeration.Perfil;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoService {

    void criarSolicitacao(Aluno aluno, TemaTCC temaTCC, Professor professor);

    void analisaSolicitacaoTemaProfessor(Long idOrientacao, Boolean respostaSolicitacaoOrientacao, Perfil perfil, String mensagemResposta) throws Exception;

    void analisaSolicitacaoTemaAluno(Long idSolicitacao, Boolean respostaSolicitacao, String mensagem) throws Exception;

    List<SolicitacaoResponseDTO> listarSolicitacoesTemaTCCProf(UUID idProf);
}
