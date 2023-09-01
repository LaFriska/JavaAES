package com.friska.javaaes.encryption;

import com.friska.javaaes.AES;
import com.friska.javaaes.cipher.Block;

import java.util.Arrays;

final public class EncryptionECB extends EncryptionType<EncryptionECB> {

    public EncryptionECB(byte[] inputBytes, byte[] key, AES aes){
        super(inputBytes, key, aes);
    }

    @Override
    public EncryptionECB encrypt() {
        plaintext = Padding.pad(plaintext);
        resetPointer();
        while(pointer + 16 <= plaintext.length){
            System.arraycopy(new Block(Arrays.copyOfRange(plaintext, pointer, pointer + 16), schedule).encrypt().getOutputBytes(), 0, plaintext, pointer,16);
            pointer += 16;
        }
        return this;
    }

    @Override
    public EncryptionECB decrypt() {
        plaintext = Padding.unpad(plaintext);
        resetPointer();
        while(pointer + 16 < plaintext.length){
            System.arraycopy(new Block(Arrays.copyOfRange(plaintext, pointer, pointer + 16), schedule).decrypt().getOutputBytes(), 0, plaintext, pointer,16);
            pointer += 16;
        }
        return this;
    }

    public byte[] getBytes() {
        return plaintext;
    }
}
