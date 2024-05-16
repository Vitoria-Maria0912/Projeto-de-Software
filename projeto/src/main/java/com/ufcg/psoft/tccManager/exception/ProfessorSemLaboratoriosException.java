package com.ufcg.psoft.tccManager.exception;

public class ProfessorSemLaboratoriosException extends TccException {
    public ProfessorSemLaboratoriosException() {
        super("Professor deve pertencer a, pelo menos um, laboratorio!");
    }
}
