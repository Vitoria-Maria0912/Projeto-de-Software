package com.ufcg.psoft.tccManager.exception;

public class AcessoNegadoException extends TccException {
    public AcessoNegadoException() {
        super("Usuario nao autorizado realizar esta acao!");
    }
}