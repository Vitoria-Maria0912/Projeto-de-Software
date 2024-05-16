package com.ufcg.psoft.tccManager.exception;

public class AlunoNaoExisteException extends TccException {
    public AlunoNaoExisteException() {
        super("O aluno consultado nao existe!");
    }
}
