package com.friska.javaaes.encryption;

import com.friska.javaaes.exceptions.PaddingException;

/**
 * This is the class used to add padding to the input plaintext before it is encrypted.
 * The number of bytes added to the padding depends on the length of
 * the byte modulo 16. The number of padding bytes added is equal to 16 - (L % 16) where L is the length of
 * the byte array. This means that even if the length of the byte array is divisible by 16, padding is added,
 * such that the program does not need to compute whether a removal of padding is required during decryption.
 * */
public class Padding {

    private static final byte PADDING_BYTE = 0x00;

    public static byte[] pad(byte[] bytes){
        final int n = bytes.length % 16;
        int p = n == 0 ? 16 : 16 - n;
        byte[] res = new byte[bytes.length + p];
        System.arraycopy(bytes, 0, res, 0, bytes.length);
        for(int i = bytes.length; i < res.length; i++){
            res[i] = (byte) (i == res.length - 1 ? p : PADDING_BYTE);
        }
        return res;
    }

    public static byte[] unpad(byte[] bytes){
        int p = bytes[bytes.length - 1];
        if(p < 1 ||p > 16) throw new PaddingException("Invalid final byte. Final byte must numerically represent a number from 1 to 16.");
        byte[] res = new byte[bytes.length - p];
        System.arraycopy(bytes, 0, res, 0 ,res.length);
        return res;
    }

}
