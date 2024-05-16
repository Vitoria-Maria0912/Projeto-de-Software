package com.ufcg.psoft.tccManager.exception;

public class ProfessorSemTemasTccCadastradosException extends TccException {
    public ProfessorSemTemasTccCadastradosException() {
        super("Professor não tem nenhum TemaTcc Cadastrado!");
    }
}
