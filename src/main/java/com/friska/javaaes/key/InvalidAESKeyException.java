package com.friska.javaaes.key;

import javax.annotation.Nonnull;

public class InvalidAESKeyException extends RuntimeException{

    public InvalidAESKeyException(@Nonnull String msg) {
        super("An error occurred processing an AES key. " + msg);
    }

}
