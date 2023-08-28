package com.friska.javaaes.exceptions;

public class PaddingException extends RuntimeException{

    public PaddingException(String msg){
        super("An issue occurred processing padding for input bytes. " + msg);
    }

}
