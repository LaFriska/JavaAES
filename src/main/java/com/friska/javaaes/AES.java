package com.friska.javaaes;

public enum AES {

    AES_128(128, 10),
    AES_192(192, 12),
    AES256(256, 14)

    ;

    public final int keysize;

    public final int rounds;
    AES(int keysize, int rounds){
        this.keysize = keysize;
        this.rounds = rounds;
    }

}
