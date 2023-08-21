package com.friska.javaaes.cipher;

public class GaloisOperation {
    /**
     * Below is a byte representation of the polynomial used for multiplication of
     * bytes in the Galois Field of 2⁸. This constant is chosen for the Rijndael cipher to
     * modulo reduce the galois field multiplication.
     * */
    public static final byte MODULO_POLYNOMIAL = 0x1b;

    /**
     * Multiplies the given byte by 2 in the Galois Field of 2⁸ modulo the modulo polynomial determined specifically in Rijndael.
     * The implemented method to computationally efficiently perform this mathematical operation is to apply a bit-wise
     * circular shift of 1 to the input byte, and check whether the most significant bit of
     * the original input byte was set. If such condition is met, then the modulo polynomial will be added to the
     * result via a bitwise XOR.
     * */
    public static byte xTimes(byte value){
        byte result = (byte) (value << 1);
        if ((value & 0x80) != 0) {
            result ^= MODULO_POLYNOMIAL;
        }
        return result;
    }
}
