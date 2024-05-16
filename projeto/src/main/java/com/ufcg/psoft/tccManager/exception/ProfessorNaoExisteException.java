package com.ufcg.psoft.tccManager.exception;

public class ProfessorNaoExisteException extends TccException {
    public ProfessorNaoExisteException() {
        super("O professor consultado nao existe!");
    }
}
