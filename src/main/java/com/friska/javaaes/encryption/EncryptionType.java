package com.friska.javaaes.encryption;

import com.friska.javaaes.AES;
import com.friska.javaaes.cipher.Cipher;
import com.friska.javaaes.key.AESKey;
import com.friska.javaaes.key.KeyExpander;
import com.friska.javaaes.key.KeySchedule;

abstract public class EncryptionType<T extends Cipher<T>> implements Cipher<T> {

    protected byte[] plaintext;
    protected int pointer;

    protected final KeySchedule schedule;

    EncryptionType(byte[] inputBytes, KeySchedule schedule){
        resetPointer();
        this.plaintext = inputBytes;
        this.schedule = schedule;
    }

    EncryptionType(byte[] inputBytes, byte[] key, AES aes){
        this(inputBytes, new KeyExpander(new AESKey(key), aes).expand().getSchedule());
    }

    protected void resetPointer(){
        this.pointer = 0;
    }
}
