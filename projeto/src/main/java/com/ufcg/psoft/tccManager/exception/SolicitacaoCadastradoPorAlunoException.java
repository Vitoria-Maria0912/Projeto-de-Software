package com.ufcg.psoft.tccManager.exception;

public class SolicitacaoCadastradoPorAlunoException extends TccException {
    public  SolicitacaoCadastradoPorAlunoException() {
        super("A solicitacao tem TemaTCC cadastrado por Aluno !");
    }
}
