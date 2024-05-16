package com.ufcg.psoft.tccManager.exception;

public class AreaDeEstudoNaoExisteException extends TccException{
    public AreaDeEstudoNaoExisteException() {
        super("A area de estudo consultada nao existe!");
    }
}
