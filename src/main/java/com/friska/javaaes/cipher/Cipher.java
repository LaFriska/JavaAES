package com.friska.javaaes.cipher;

public interface Cipher<T extends Cipher<T>> {

    T encrypt();
    T decrypt();

}
