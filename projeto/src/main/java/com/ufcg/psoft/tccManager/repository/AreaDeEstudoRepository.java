package com.ufcg.psoft.tccManager.repository;

import com.ufcg.psoft.tccManager.model.AreaDeEstudo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AreaDeEstudoRepository extends JpaRepository<AreaDeEstudo, Long> {
    boolean existsByNome(String nome);
    Optional<AreaDeEstudo> getByNome(String nome);
}
