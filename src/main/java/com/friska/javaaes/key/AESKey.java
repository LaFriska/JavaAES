package com.friska.javaaes.key;

public class AESKey {

    public final byte[] bytes;

    public AESKey(byte[] bytes){
        if(bytes.length % 4 != 0) throw new InvalidAESKeyException("Key length modulo 4 is not equal to 0.");
        this.bytes = bytes;
    }

    public byte[] get(){
        return bytes;
    }

    public int size(){
        return bytes.length;
    }

}
