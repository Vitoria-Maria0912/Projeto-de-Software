package com.ufcg.psoft.tccManager.exception;

public class SolicitacaoNaoExisteException extends TccException {
    public  SolicitacaoNaoExisteException() {
        super("A solicitacao nao existe!");
    }
}
