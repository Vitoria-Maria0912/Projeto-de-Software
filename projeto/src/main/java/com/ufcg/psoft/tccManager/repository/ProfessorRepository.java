package com.ufcg.psoft.tccManager.repository;

import com.ufcg.psoft.tccManager.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ProfessorRepository extends JpaRepository<Professor, UUID> {

    @Query(value = "SELECT p FROM Professor p WHERE p.quota > 0")
    List<Professor> getProfessorWithOrientationQuota();

    Professor getProfessorById(UUID id);

    @Query("SELECT p FROM Professor p WHERE p.temasTcc IS NOT EMPTY")
    List<Professor> getProfessoresTemasTCCIsNotNull();


}
