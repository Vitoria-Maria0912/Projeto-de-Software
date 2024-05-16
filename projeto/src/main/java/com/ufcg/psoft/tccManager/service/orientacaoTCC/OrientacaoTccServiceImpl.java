package com.ufcg.psoft.tccManager.service.orientacaoTCC;

import com.ufcg.psoft.tccManager.model.*;
import com.ufcg.psoft.tccManager.repository.OrientacaoTccRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrientacaoTccServiceImpl implements OrientacaoTccService {

    @Autowired
    OrientacaoTccRepository orientacaoTccRepository;

    @Override
    public void criarOrientacao(Aluno aluno, TemaTCC temaTcc, Professor professor) {
        OrientacaoTcc novaOrientacaoTcc = new OrientacaoTcc();
        novaOrientacaoTcc.setAluno(aluno);
        novaOrientacaoTcc.setProfessor(professor);
        novaOrientacaoTcc.setTemaTcc(temaTcc);

        this.orientacaoTccRepository.save(novaOrientacaoTcc);
    }
}
