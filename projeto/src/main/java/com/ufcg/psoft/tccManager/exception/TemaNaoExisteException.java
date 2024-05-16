package com.ufcg.psoft.tccManager.exception;

public class TemaNaoExisteException extends TccException {
    public TemaNaoExisteException() {
        super("O tema consultado nao existe!");
    }
}
