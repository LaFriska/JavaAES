package com.friska.javaaes;

import com.friska.javaaes.key.AESKey;
import com.friska.javaaes.exceptions.InvalidAESKeyException;
import com.friska.javaaes.exceptions.InvalidInputBytesException;

@Deprecated
public class AESBlockCipher {


    private byte[][] state;

    private AES type;

    private AESKey key;

    public AESBlockCipher(byte[] inputBytes, AES aesType, AESKey key){
        precond(inputBytes, aesType, key);
        this.state = copyToState(inputBytes);
        this.type = aesType;
        this.key = key;
    }

    private byte[][] copyToState(byte[] inputByte){
        byte[][] state = new byte[4][4];
        int col = 0;
        for(int i = 0; i < inputByte.length; i++){
            col = i / 4;
            state[i % 4][col] = inputByte[i];
        }
        return state;
    }

    private void precond(byte[] inputBytes, AES aesType, AESKey key){
        if(key.wordsSize() != aesType.keysize / 8)
            throw  new InvalidAESKeyException("AES key size must match that of the chosen AES instance, " +
                    "in this case, it should be "
                    + aesType.keysize/8 +" bytes long.");
        if(inputBytes.length != 16)
            throw new InvalidInputBytesException("Cannot process input byte of length " + inputBytes.length + " for an AES block size of 16.");
    }

}
