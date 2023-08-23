package com.friska.javaaes.encryption;

import com.friska.javaaes.exceptions.InvalidInputBytesException;

public class EncryptionECB {

    private final byte[] buffer;

    public EncryptionECB(byte[] inputByteBuffer){
        if(inputByteBuffer.length % 16 != 0) throw new InvalidInputBytesException("Input bytes must be divisible by 16.");
        this.buffer = inputByteBuffer;
    }



}
