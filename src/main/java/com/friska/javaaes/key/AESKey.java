package com.friska.javaaes.key;

import com.friska.javaaes.exceptions.InvalidAESKeyException;
import com.friska.javaaes.util.Assert;

public class AESKey {
    public final Word[] words;

    public final byte[] bytes;

    public AESKey(byte[] bytes){
        if(bytes.length % 4 != 0) throw new InvalidAESKeyException("Key length modulo 4 is not equal to 0.");
        this.words = getAsWords(bytes);
        this.bytes = bytes;
    }

    private Word[] getAsWords(byte[] bytes){
        Assert.a(bytes.length % 4 == 0);
        Word[] words = new Word[bytes.length/4];
        int wordPointer = 0;
        byte[] tempBytes = null;

        for(int i = 0; i < bytes.length; i++){
            if(i % 4 == 0) {
                if(tempBytes != null) {
                    words[wordPointer] = new Word(tempBytes);
                    wordPointer++;
                }
                tempBytes = new byte[4];
            }
            tempBytes[i % 4] = bytes[i];
        }
        words[wordPointer] = new Word(tempBytes);
        return words;
    }

    /**
     * Referred to as Nk in the official documentation.
     * **/
    public int wordsSize(){
        return words.length;
    }

    public int keySize(){
        return bytes.length;
    }

}
