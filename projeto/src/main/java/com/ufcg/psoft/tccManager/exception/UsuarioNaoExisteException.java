package com.ufcg.psoft.tccManager.exception;

public class UsuarioNaoExisteException extends TccException {
    public UsuarioNaoExisteException() {
        super("O identificador é invalido, o usuario nao existe!");
    }
}
