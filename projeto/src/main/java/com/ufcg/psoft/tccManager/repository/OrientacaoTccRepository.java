package com.ufcg.psoft.tccManager.repository;


import com.ufcg.psoft.tccManager.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface OrientacaoTccRepository extends JpaRepository<OrientacaoTcc, Long> {

    @Query(value = "SELECT t FROM OrientacaoTcc o RIGHT JOIN temaTcc t WHERE o.temaTcc IS NULL")
    List<TemaTCC> getTemasNaoAlocados();

    @Query(value = "SELECT a FROM OrientacaoTcc o RIGHT JOIN aluno a WHERE o.aluno IS NULL")
    List<Aluno> getAlunosSemOrientador();

    @Query(value = "SELECT p FROM OrientacaoTcc o RIGHT JOIN professor p WHERE o.professor IS NULL")
    List<Professor> getProfessoresSemOrientandos();
}
