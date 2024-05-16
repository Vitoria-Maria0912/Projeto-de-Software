package com.ufcg.psoft.tccManager.exception;

public class NaoHaAreasDeEstudoException extends TccException {
    public NaoHaAreasDeEstudoException() {
        super("Nao ha nenhuma area de estudo cadastrada para esse usuario!");
    }
}