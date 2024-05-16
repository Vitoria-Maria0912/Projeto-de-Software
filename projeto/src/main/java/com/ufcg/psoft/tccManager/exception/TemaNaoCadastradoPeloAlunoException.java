package com.ufcg.psoft.tccManager.exception;

public class TemaNaoCadastradoPeloAlunoException extends TccException {
    public TemaNaoCadastradoPeloAlunoException() {
        super("O tema consultado nao foi cadastrado por um aluno!");
    }
}
