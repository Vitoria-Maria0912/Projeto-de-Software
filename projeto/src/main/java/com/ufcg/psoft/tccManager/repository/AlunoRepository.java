package com.ufcg.psoft.tccManager.repository;

import com.ufcg.psoft.tccManager.model.Aluno;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlunoRepository extends JpaRepository<Aluno, UUID> {

    Aluno getAlunoById(UUID id);

    @Query(value = "SELECT a FROM Aluno a WHERE a.temasTcc IS NOT EMPTY")
    List<Aluno> getAlunosTemasTCCIsNotNull();
}
