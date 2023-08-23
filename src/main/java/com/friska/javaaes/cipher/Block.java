package com.friska.javaaes.cipher;

import com.friska.javaaes.key.KeySchedule;
import com.friska.javaaes.util.Assert;
import com.friska.javaaes.util.ByteUtil;

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

    public Block(byte[] inputBytes, KeySchedule schedule){
        Assert.a(inputBytes.length == 16);
        this.schedule = schedule;
        nr = schedule.getAES().rounds;
        state = formatState(inputBytes);
        if(DEBUG) printState("INITIAL STATE");
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
        add(schedule.getRoundKey(0));
        for(int i = 1; i <= nr - 1; i++){
            if(DEBUG) System.out.println("----------------ROUND " + i + "---------------------------");
            subBytes(false);
            shiftRows(false);
            mixColumns();
            add(schedule.getRoundKey(i));
        }
        if(DEBUG) System.out.println("----------------ROUND " + nr + "---------------------------");
        subBytes(false);
        shiftRows(false);
        add(schedule.getRoundKey(nr));
        return this;
    }

    @Override
    public Block decrypt(){
        add(schedule.getRoundKey(nr));
        for(int i = nr - 1; i >= 1; i--){
            shiftRows(true);
            subBytes(true);
            add(schedule.getRoundKey(i));

        }
    }

    private void mixColumns(){
        for(int i = 0; i <= 3; i++){
            byte[] mixed = getMixedColumn(i);
            for(int a = 0; a < mixed.length; a++){
                state[a][i] = mixed[a];
            }
        }
        if(DEBUG) printState("MIX COLUMNS");
    }

    private byte[] getMixedColumn(int col){
        byte[] res = new byte[4];
        byte temp;
        for(int matrixRow = 0; matrixRow <= 3; matrixRow++){
            temp = 0;
            for(int i = 0; i <= 3; i++){
                temp ^= multiply(state[i][col], THE_MATRIX[matrixRow][i]);
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
        if(DEBUG) printState("SUB BYTES");
    }

    private void shiftRows(boolean inv){
        for(int i = 1; i <= 3; i++) {
            getShiftedRow(state[i], inv ? -i : i);
        }
        if(DEBUG) printState("SHIFT ROWS");
    }

    private void getShiftedRow(byte[] row, int offset){
        byte[] copy = row.clone();
        for (int i = 0; i < copy.length; i++) {
            row[i] = copy[(i + offset + 4) % 4];
        }
        copy = null; //Deletes the pointer
    }

    public Block add(byte[] bytes){
        int col = -1;
        int row;
        for(int i = 0; i <= 15; i++){
            row = i % 4;
            if(i % 4 == 0) col++;
            state[row][col] = (byte) (state[row][col] ^ bytes[i]);
        }
        if(DEBUG) printState("ADD KEY");
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
}
