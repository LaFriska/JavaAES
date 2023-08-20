package com.friska.javaaes;

import com.friska.javaaes.key.AESKey;
import com.friska.javaaes.exceptions.InvalidAESKeyException;
import com.friska.javaaes.exceptions.InvalidInputBytesException;

public class AESBlockCipher {

    /**
     * The state block is represented as a 2D array of the following configuration:
     * <br><br>
     * state[rowIndex][columnIndex]
     * <br><br>
     * where the rowIndex goes up and down while the column goes left and right.
     * <br><br>
     * [0,0][0,1][0,2][0,3]<br>
     * [1,0][1,1][1,2][1,3]<br>
     * [2,0][2,1][2,2][2,3]<br>
     * [3,0][3,1][3,2][3,3]<br>
     * **/
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
