package com.ufcg.psoft.tccManager.exception;

public class TccException extends RuntimeException{
    public TccException(){
        super("Erro inesperado no TCC Manager!");
    }

    public TccException(String message){
        super(message);
    }    
}
