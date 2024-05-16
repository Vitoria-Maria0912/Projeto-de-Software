package com.ufcg.psoft.tccManager.service.orientacaoTCC;

import com.ufcg.psoft.tccManager.model.*;

public interface OrientacaoTccService {
    void criarOrientacao(Aluno aluno, TemaTCC temaSelecionado, Professor professor);
}
