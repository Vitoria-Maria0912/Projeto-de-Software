package com.ufcg.psoft.tccManager.exception;

public class SolicitacaoJaRespondidaExcepetion extends TccException {
    public  SolicitacaoJaRespondidaExcepetion() {
        super("A solicitacao ja foi respondida!");
    }
}
