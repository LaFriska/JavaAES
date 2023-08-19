package com.friska.javaaes.key;

import com.friska.javaaes.util.Assert;

import javax.annotation.Nonnull;

public class Word implements Cloneable{

    public byte[] bytes;
    public Word(@Nonnull byte... bytes){
        Assert.a(bytes.length == 4);
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        return sb.toString();
    }

    @Override
    protected Word clone() {
        return new Word(bytes.clone());
    }
}
