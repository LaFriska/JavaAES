package com.friska.javaaes;

import com.friska.javaaes.util.ByteUtil;

public class Encryption {

    /**
     * This is the class used to add padding to the input plaintext before it is encrypted
     * via AES. The number of bytes added to the padding depends on the modulo 16 of the length of
     * the byte. The number of padding bytes added is equal to 16 - (L % 16) where L is the length of
     * the byte array.
     * */
    public static class Padding{

        private byte[] bytes;
        private final int n;

        public Padding(byte[] bytes){
            this.bytes = bytes;
            n = bytes.length % 16;
        }

        public Padding pad(){
            int p = n == 0 ? 16 : 16 - n;
            byte[] res = new byte[bytes.length + p];
            System.arraycopy(bytes, 0, res, 0, bytes.length);
            for(int i = bytes.length; i < res.length; i++){
                res[i] = (byte) (i == res.length - 1 ? p : 0x00);
            }
            bytes = res;
            return this;
        }

        public Padding unpad(){



            return this;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }
}
