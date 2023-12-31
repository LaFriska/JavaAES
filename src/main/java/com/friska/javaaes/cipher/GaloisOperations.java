package com.friska.javaaes.cipher;

import com.friska.javaaes.util.Assert;

public class GaloisOperations {
    /**
     * Below is a byte representation of the polynomial used for multiplication of
     * bytes in the Galois Field of 2⁸. This constant is chosen for the Rijndael cipher to
     * modulo reduce the galois field multiplication.
     * */
    public static final byte MODULO_POLYNOMIAL = 0x1b;

    /**
     * Below is a 2D array implementation of the fixed matrix used for the mixColumns step in
     * Rijndael. The matrix contains of numbers 1 to 3, to ensure computational efficiency, and
     * designed in a way maximum diffusion and non-linearity is promised in the cipher. During
     * the mixColumns step, each column is represented by a vector and multiplied by this matrix.
     * The resulting vector will then replace the column.
     * */
    public static final int[][] THE_MATRIX = new int[][]{
            new int[]{2,3,1,1},
            new int[]{1,2,3,1},
            new int[]{1,1,2,3},
            new int[]{3,1,1,2}
    };

    /**
     * Below is the matrix used for the invMixColumns step, as each element is
     * carefully chose to undo the diffusion created by mixColumns.
     * */
    public static final int[][] INVERSE_MATRIX = new int[][]{
            new int[]{14,11,13,9},
            new int[]{9,14,11,13},
            new int[]{13,9,14,11},
            new int[]{11,13,9,14}
    };

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

    public static byte multiply(byte value, int multiplier){
        switch (multiplier){
            case 1 -> {
                return value;
            }
            case 2 -> {
                return xTimes(value);
            }
            case 3 -> {
                return (byte) (xTimes(value) ^ value);
            }
            case 8 ->{
                //This case is only used for the sake of recursion of multiple() method.
                return xTimes(xTimes(xTimes(value)));
            }
            case 9 -> {
                return (byte) (multiply(value, 8) ^ value);
            }
            case 11 -> {
                return (byte) (multiply(value, 8) ^ multiply(value, 3));
            }
            case 13 -> {
                return (byte) (multiply(value, 8) ^ xTimes(xTimes(value)) ^ value);
            }
            case 14 -> {
                return (byte) (multiply(value, 8) ^ xTimes(multiply(value, 3)));
            }
            default -> throw new RuntimeException("Cannot perform galois multiplication on " + multiplier +".");
        }
    }
}
