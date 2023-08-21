package com.friska.javaaes.key;

import com.friska.javaaes.AES;
import com.friska.javaaes.cipher.SBox;
import com.friska.javaaes.util.Assert;
import com.friska.javaaes.util.ByteUtil;

import javax.annotation.Nonnull;

public class KeyExpander {

    /**
     * AES round constants. These constants should never be changed, as they are
     * explicitly defined in the official AES documentation. Depending on the key
     * size, each of the constants defined below is used to ensure non-linearity in the
     * key expansion for different rounds. Each round constant composes of a word, or
     * 4 bytes of data.
     * **/
    private static final Word[] RCON = new Word[]{
            new Word((byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x40, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x1b, (byte) 0x00, (byte) 0x00, (byte) 0x00),
            new Word((byte) 0x36, (byte) 0x00, (byte) 0x00, (byte) 0x00)
    };

    private final AESKey key;
    private final AES aes;

    private final Word[] words;

    private KeySchedule schedule = null;
    public KeyExpander(AESKey key, AES aes){
        Assert.a(key.keySize() == aes.keysize / 8);
        this.key = key;
        this.aes = aes;
        words = initializeExpandedKeyArray(key, aes);
    }

    private Word[] initializeExpandedKeyArray(AESKey key, AES aes){
        Word[] res = new Word[4 * aes.rounds + 4];
        System.arraycopy(key.words, 0, res, 0, key.wordsSize());
        return res;
    }

    public Word[] getWords() {
        return words;
    }

    public KeySchedule getSchedule() {
        return schedule;
    }

    public void expand(){
        Word temp;
        byte[] tempBytes;
        int nk = key.wordsSize();
        for(int i = nk; i < words.length; i++){
            temp = words[i - 1].clone();
            if(i % nk == 0){
                temp = addConstant(subWord(rotWord(temp)), i/nk - 1);
            }else if(nk > 6 && i % nk == 4){
                subWord(temp);
            }
            words[i] = ByteUtil.xorWords(words[i - nk], temp);
        }
        schedule = new KeySchedule(words, aes);
    }

    private Word rotWord(@Nonnull Word word){
        byte temp = word.bytes[0];
        for(int i = 1; i < 4; i++){
            word.bytes[i - 1] = word.bytes[i];
        }
        word.bytes[3] = temp;
        return word;
    }

    private Word subWord(@Nonnull Word word){
        for(int i = 0; i < 4; i++){
            word.bytes[i] = SBox.getSub(word.bytes[i]);
        }
        return word;
    }

    private Word addConstant(@Nonnull Word word, int constantIndex){
        return ByteUtil.xorWords(word, RCON[constantIndex]);
    }

}
