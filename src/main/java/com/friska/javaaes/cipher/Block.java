package com.friska.javaaes.cipher;

import com.friska.javaaes.key.KeySchedule;
import com.friska.javaaes.key.Word;
import com.friska.javaaes.util.Assert;
import com.friska.javaaes.util.ByteUtil;

public class Block {

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
    private byte[/*Row index*/]
            [/*Column index*/]
            state;

    private final KeySchedule schedule;

    public Block(byte[] inputBytes, KeySchedule schedule){
        Assert.a(inputBytes.length == 16);
        state = formatState(inputBytes);
        this.schedule = schedule;
    }


    private byte[][] formatState(byte[] inputBytes){
        byte[][] res = new byte[4][4];
        int row = -1;
        int col;
        for(int i = 0; i <= 15; i++){
            col = i % 4;
            if(col == 0) row++;
            res[col][row] = inputBytes[i];
        }
        return res;
    }

    private Block encrypt(){

        int nr = schedule.getAES().rounds;

        xorWith(schedule.getRoundKey(0));
        for(int i = 1; i <= nr - 1; i++){
            subBytes();
            shiftRows();
        }
        return this;
    }

    private void mixColumns(){

    }

    private void subBytes(){
        int row = -1;
        int col;
        for(int i = 0; i <= 15; i++){
            col = i % 4;
            if(col == 0) row++;
            state[col][row] = SBox.getSub(state[col][row]);
        }
    }

    private void shiftRows(){
        for(int i = 1; i <= 3; i++) state[i] = getShiftedRow(state[i], i);
    }

    private byte[] getShiftedRow(byte[] row, int offset){
        byte[] copy = row.clone();
        for (int i = 0; i < copy.length; i++) {
            row[i] = copy[(i + offset) % 4];
        }
        copy = null; //Readies it for gc
        return row;
    }

    public Block xorWith(byte[] bytes){
        int row = -1;
        int col;
        for(int i = 0; i <= 15; i++){
            col = i % 4;
            if(i % 4 == 0) row++;
            state[col][row] = (byte) (state[col][row] ^ bytes[i]);
        }
        return this;
    }

}
