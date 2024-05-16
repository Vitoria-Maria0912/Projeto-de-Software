package com.ufcg.psoft.tccManager.repository;


import com.ufcg.psoft.tccManager.model.Solicitacao;
import com.ufcg.psoft.tccManager.model.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoRepository  extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> getSolicitacoesTccByProfessor_IdAndTemaTcc_Status(UUID idProf, Status status);
}
