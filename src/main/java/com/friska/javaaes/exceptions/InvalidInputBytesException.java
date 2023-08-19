package com.friska.javaaes.exceptions;

import javax.annotation.Nonnull;

public class InvalidInputBytesException extends RuntimeException{

    public InvalidInputBytesException(@Nonnull String msg){
        super("An error occurred processing input bytes. " + msg);
    }

}
