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
        hexString = hexString.toLowerCase().replaceAll(" ", "");
        Assert.a(hexString.length() % 2 == 0);
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

    public static String byteArrayToHexString(byte[] byteArray) {return byteArrayToHexString(byteArray, false);}

    public static String byteArrayToHexString(byte[] byteArray, boolean addSpace) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : byteArray) {
            String hex = String.format("%02X", b & 0xFF); // Convert byte to 2-digit hex
            hexString.append(hex);
            if(addSpace) hexString.append(" ");
        }

        // Removes the finalZ space
        if (!hexString.isEmpty() && addSpace) {
            hexString.setLength(hexString.length() - 1);
        }

        return hexString.toString();
    }
}
