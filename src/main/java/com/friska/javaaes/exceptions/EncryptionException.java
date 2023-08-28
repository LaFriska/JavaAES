package com.friska.javaaes.exceptions;

public class EncryptionException extends RuntimeException{

    public EncryptionException(String msg){
        super("An error occurred trying to process an encryption. " + msg);
    }

}
