package com.friska.javaaes.encryption;

import com.friska.javaaes.AES;
import com.friska.javaaes.cipher.Block;
import com.friska.javaaes.cipher.Cipher;
import com.friska.javaaes.exceptions.InvalidInputBytesException;
import com.friska.javaaes.key.AESKey;
import com.friska.javaaes.key.KeyExpander;
import com.friska.javaaes.key.KeySchedule;

import java.util.Arrays;

public class EncryptionECB implements Cipher<EncryptionECB> {

    private final byte[] buffer;
    private int pointer;

    private final KeySchedule schedule;

    public EncryptionECB(byte[] inputByteBuffer, KeySchedule schedule){
        if(inputByteBuffer.length % 16 != 0) throw new InvalidInputBytesException("Input bytes must be divisible by 16.");
        resetPointer();
        this.buffer = inputByteBuffer;
        this.schedule = schedule;
    }

    public EncryptionECB(byte[] inputByteBuffer, byte[] key, AES aes){
        this(inputByteBuffer, new KeyExpander(new AESKey(key), aes).expand().getSchedule());
    }

    private void resetPointer(){
        this.pointer = 0;
    }

    @Override
    public EncryptionECB encrypt() {
        resetPointer();
        while(pointer + 16 <= buffer.length){
            System.arraycopy(new Block(Arrays.copyOfRange(buffer, pointer, pointer + 16), schedule).encrypt().getOutputBytes(), 0, buffer, pointer,16);
            pointer += 16;
        }
        return this;
    }

    @Override
    public EncryptionECB decrypt() {
        resetPointer();
        while(pointer + 16 < buffer.length){
            System.arraycopy(new Block(Arrays.copyOfRange(buffer, pointer, pointer + 16), schedule).decrypt().getOutputBytes(), 0, buffer, pointer,16);
            pointer += 16;
        }
        return this;
    }

    public byte[] getBytes() {
        return buffer;
    }
}
