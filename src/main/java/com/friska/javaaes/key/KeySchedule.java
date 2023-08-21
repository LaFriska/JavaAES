package com.friska.javaaes.key;

import com.friska.javaaes.AES;

public class KeySchedule {

    private final byte[/*Round number*/][/*Byte number*/] schedule;
    private final AES aes;

    public KeySchedule(Word[] wordArray, AES aes){
        this.aes = aes;
        this.schedule = getSchedule(wordArray);
    }

    private byte[][] getSchedule(Word[] wordArray){
        byte[][] res = new byte[aes.rounds + 1][16];
        int roundCount = -1;
        for (int i = 0; i < wordArray.length; i++) {
            if(i % 4 == 0) roundCount++;
            for(int a = 0; a <= 3; a++) {
                res[roundCount][(i % 4) * 4 + a] = wordArray[i].bytes[a];
            }
        }
        return res;
    }

    public AES getAES() {
        return aes;
    }

    /**
     * Input the number identifier of the round as a parameter. For the key XORed
     * into the state array before the round start, input "0" as round.
     * **/
    public byte[] getRoundKey(int round){
        return schedule[round];
    }

}
