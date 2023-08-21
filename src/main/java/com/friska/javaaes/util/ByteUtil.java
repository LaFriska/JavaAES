package com.friska.javaaes.util;

import com.friska.javaaes.key.Word;

public class ByteUtil {


    public static Word xorWords(Word a, Word b) {
        byte[] bytes = new byte[4];
        for(int i = 0; i <= 3; i++){
            bytes[i] = (byte) (a.getBytes()[i] ^ b.getBytes()[i]);
        }
        return new Word(bytes);
    }

    public static byte[] getBytesFromHexString(String hexString){
        Assert.a(hexString.length() % 2 == 0);
        hexString = hexString.toLowerCase().replaceAll(" ", "");
        boolean sixteenth = true;
        String hexSet = "0123456789abcdef";
        int value = 0;
        byte[] bytes = new byte[hexString.length() / 2];
        for(int i = 0; i < hexString.length(); i++){
            int val = hexSet.indexOf(hexString.charAt(i));
            if(sixteenth){
                value = val * 16;
            }else{
                value = value + val;
                if(value > 127) value = value - 256;
                bytes[i / 2] = (byte) value;
            }
            sixteenth = !sixteenth;
        }
        return bytes;
    }
}
