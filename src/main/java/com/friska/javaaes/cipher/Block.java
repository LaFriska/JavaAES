package com.friska.javaaes.cipher;

import com.friska.javaaes.key.KeySchedule;
import com.friska.javaaes.util.Assert;
import com.friska.javaaes.util.ByteUtil;

import javax.annotation.Nullable;

import static com.friska.javaaes.cipher.GaloisOperations.*;

public class Block implements Cipher<Block>{

    private static final boolean DEBUG = true;

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
     * <br><br>
     * In java, the state array may also be represented as an array of size 4 of 4 other byte arrays of size 4.<br><br>
     *
     * [<br>
     *    [B0, B1, B2, B3]<br>
     *    [B0, B1, B2, B3]<br>
     *    [B0, B1, B2, B3]<br>
     *    [B0, B1, B2, B3]<br>
     * ]<br>
     * **/
    private final byte[/*Row index*/][/*Column index*/] state;

    private final KeySchedule schedule;
    private final int nr;

    /**
     * The initialization vector is a way to chain the block to either a previous block
     * or a pre-defined vector before the block is encrypted. This is used especially in the
     * CBC mode of operation. When no initialization vector is to be used, null should be parsed
     * into the IV parameter in the constructor.
     * */
    @Nullable
    private final byte[] iv;

    public Block(byte[] inputBytes, KeySchedule schedule, @Nullable byte[] initialisationVector){
        Assert.a(inputBytes.length == 16);
        this.schedule = schedule;
        nr = schedule.getAES().rounds;
        state = formatState(inputBytes);
        this.iv = initialisationVector;
        debug("INITIAL STATE");
    }

    public Block(byte[] inputBytes, KeySchedule schedule){
        this(inputBytes, schedule, null);
    }


    private byte[][] formatState(byte[] inputBytes){
        byte[][] res = new byte[4][4];
        int col = -1;
        int row;
        for(int i = 0; i <= 15; i++){
            row = i % 4;
            if(row == 0) col++;
            res[row][col] = inputBytes[i];
        }
        return res;
    }

    @Override
    public Block encrypt(){
        if(iv != null) addRoundKey(iv);
        addRoundKey(schedule.getRoundKey(0));
        for(int i = 1; i <= nr - 1; i++){
            debug("----------------ROUND " + i + "---------------------------");
            subBytes(false);
            shiftRows(false);
            mixColumns(false);
            addRoundKey(schedule.getRoundKey(i));
        }
        debug("----------------ROUND " + nr + "---------------------------");
        subBytes(false);
        shiftRows(false);
        addRoundKey(schedule.getRoundKey(nr));
        return this;
    }

    @Override
    public Block decrypt(){
        debug("----------------ROUND " + nr + "---------------------------");
        addRoundKey(schedule.getRoundKey(nr));
        for(int i = nr - 1; i >= 1; i--){
            shiftRows(true);
            subBytes(true);
            debug("----------------ROUND " + i + "---------------------------");
            addRoundKey(schedule.getRoundKey(i));
            mixColumns(true);
        }
        shiftRows(true);
        subBytes(true);
        addRoundKey(schedule.getRoundKey(0));
        if(iv != null) addRoundKey(iv);
        return this;
    }

    private void mixColumns(boolean inv){
        for(int i = 0; i <= 3; i++){
            byte[] mixed = getMixedColumn(i, inv);
            for(int a = 0; a < mixed.length; a++){
                state[a][i] = mixed[a];
            }
        }
        debug("MIX COLUMNS", inv);
    }

    private byte[] getMixedColumn(int col, boolean inv){
        byte[] res = new byte[4];
        byte temp;
        for(int matrixRow = 0; matrixRow <= 3; matrixRow++){
            temp = 0;
            for(int i = 0; i <= 3; i++){
                temp ^= multiply(state[i][col], inv ?
                        INVERSE_MATRIX[matrixRow][i]
                      : THE_MATRIX[matrixRow][i]);
            }
            res[matrixRow] = temp;
        }
        return res;
    }

    private void subBytes(boolean inv){
        int col = -1;
        int row;
        for(int i = 0; i <= 15; i++){
            row = i % 4;
            if(row == 0) col++;
            state[row][col] = SBox.getSub(state[row][col], inv);
        }
        debug("SUB BYTES", inv);
    }

    private void shiftRows(boolean inv){
        for(int i = 1; i <= 3; i++) {
            getShiftedRow(state[i], inv ? -i : i);
        }
        debug("SHIFT ROWS", inv);
    }

    private void getShiftedRow(byte[] row, int offset){
        byte[] copy = row.clone();
        for (int i = 0; i < copy.length; i++) {
            row[i] = copy[(i + offset + 4) % 4];
        }
        copy = null; //Deletes the pointer
    }

    public Block addRoundKey(byte[] bytes){
        int col = -1;
        int row;
        for(int i = 0; i <= 15; i++){
            row = i % 4;
            if(row == 0) col++;
            state[row][col] = (byte) (state[row][col] ^ bytes[i]);
        }
        debug("ADD KEY");
        return this;
    }

    public byte[] getOutputBytes() {
        byte[] res = new byte[16];
        int col = -1;
        int row;
        for(int i = 0; i <= 15; i++){
            row = i % 4;
            if(row == 0) col++;
            res[i] = state[row][col];
        }
        return res;
    }

    private void printState(String preface){
        StringBuilder sb = new StringBuilder(preface).append("\n\n");
        for(int i = 0; i <= 3; i++){
            sb.append(ByteUtil.byteArrayToHexString(state[i])).append("\n");
        }
        System.out.println(sb);
    }

    private void debug(String str){
        debug(str, false);
    }

    private void debug(String str, boolean inv){
        if(DEBUG) printState(inv ? "INVERSED " + str : str);
    }
}
