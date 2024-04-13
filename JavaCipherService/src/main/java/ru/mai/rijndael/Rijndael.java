package ru.mai.rijndael;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Rijndael {
    private static Map<Pair<Integer, Integer>, Integer> mapRounds = Map.of(
            Pair.of(128, 128), 10,
            Pair.of(192, 128), 12,
            Pair.of(256, 128), 14,
            Pair.of(128, 192), 12,
            Pair.of(192, 192), 12,
            Pair.of(256, 192), 14,
            Pair.of(128, 256), 14,
            Pair.of(192, 256), 14,
            Pair.of(256, 256), 14
    );
    private final int countRounds;
    private final char modulo;
    private final byte[][] sBox;
    private byte[][] invertSBox;
    private final int countBitsBlock;
    private final int countBitsKey;
    private byte[][] key;

    public Rijndael(byte[][] key, int countBitsBlock, int countBitsKey, char irreduciblePolynomial) {
        this.key = key;
        this.countBitsBlock = countBitsBlock;
        this.countBitsKey = countBitsKey;
        this.countRounds = mapRounds.get(Pair.of(countBitsBlock, countBitsKey));
        this.modulo = irreduciblePolynomial;
        sBox = generateSBox();
    }

    public void generateKey() {

    }

    public void cipher() {
        for (int i = 0; i < countRounds; i++) {
            //TODO
        }
    }

    public byte[][] Round(byte[][] state, byte[][] roundKey) {
        state = subBytes(state);
        state = shiftRows(state);
        state = mixColumns(state);
        state = addRoundKey(state, roundKey);
        return state;
    }

    public byte[][] subBytes(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for(int j = 0; j < state[i].length; j++) {
                byte row = (byte) ((state[i][j] & 0b11110000) >> Byte.SIZE / 2);
                byte col = (byte) (state[i][j] & 0b00001111);
                result[i][j] = sBox[row][col];
            }
        }

        return result;
    }

    public void printBits(byte number) {
        for (int i = 0; i < Byte.SIZE; i++) {
            System.out.print((number >> (Byte.SIZE - i - 1)) & 1);
        }
        System.out.println();
    }

    public byte[] subBytesArray(byte[] array) {
        byte[] result = new byte[array.length];

        for (int i = 0; i < array.length; i++) {
            int row = array[i] >> (Byte.SIZE / 2);
            int col = array[i] << (Byte.SIZE / 2) >> (Byte.SIZE / 2);
            result[i] = sBox[row][col];
        }

        return result;
    }

    public byte[][] shiftRows(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            result[i] = shiftLeft(state[i], i);
        }

        return result;
    }

    public byte[][] mixColumns(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int col = 0; col < state[0].length; col++) {
            byte[] stateColumn = new byte[] {state[0][col], state[1][col], state[2][col], state[3][col]};
            byte[] resultMultiplication = mibColumn(stateColumn);
            result[0][col] = resultMultiplication[0];
            result[1][col] = resultMultiplication[1];
            result[2][col] = resultMultiplication[2];
            result[3][col] = resultMultiplication[3];
        }

        return result;
    }

    private byte[] mibColumn(byte[] column) {
        int sizePolynomial = column.length;
        byte[] result = new byte[sizePolynomial];
        byte[][] matrix = new byte[][] {
                {2, 3, 1, 1},
                {1, 2, 3, 1},
                {1, 1, 2, 3},
                {3, 1, 1, 2}
        };

        for (int i = 0; i < matrix.length; i++) {
            byte sum = 0;

            for (int j = 0; j < matrix[i].length; j++) {
                sum = GF.sum(sum, GF.multiplicationModulo(matrix[i][j], column[j], modulo));
            }

            result[i] = sum;
        }

        return result;
    }

    public byte[][] addRoundKey(byte[][] state, byte[][] roundKey) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                result[i][j] = (byte) (state[i][j] ^ roundKey[i][j]);
            }
        }

        return result;
    }

    public byte[][] generateSBox() {
        byte[] aMatrix = new byte[] {
                (byte) 0b10001111,
                (byte) 0b11000111,
                (byte) 0b11100011,
                (byte) 0b11110001,
                (byte) 0b11111000,
                (byte) 0b01111100,
                (byte) 0b00111110,
                (byte) 0b00011111
        };
        byte f = 0x63;
        byte[][] result = new byte[16][16];

        for (int i = 0; i < 256; i++) {
            result[i / 16][i % 16] = (byte) (multiplyMatrixToVector(aMatrix, GF.invert((byte) i, modulo)) ^ f);
        }

        return result;
    }

    public void keyExpansion() {

    }

    private byte multiplyMatrixToVector(byte[] matrix, byte vector) {
        byte result = 0;

        for (int i = 0; i < Byte.SIZE; i++) {
            byte sum = 0;

            for (int j = 0; j < Byte.SIZE; j++) {
                sum ^= (byte) (((matrix[i] >> (Byte.SIZE - j - 1)) & 1) & ((vector >> j) & 1));
            }

            result ^= (byte) (sum << i);
        }

        return result;
    }

    public byte[] shiftLeft(byte[] array, int valueShift) {
        byte[] leftPart = new byte[valueShift];
        byte[] rightPart = new byte[array.length - valueShift];
        byte[] result = new byte[array.length];

        System.arraycopy(array, 0, leftPart, 0, valueShift);
        System.arraycopy(array, valueShift, rightPart, 0, array.length - valueShift);
        System.arraycopy(rightPart, 0, result, 0, array.length - valueShift);
        System.arraycopy(leftPart, 0, result, array.length - valueShift, valueShift);

        return result;
    }

    public byte[] shiftRight(byte[] array, int valueShift) {
        byte[] leftPart = new byte[array.length - valueShift];
        byte[] rightPart = new byte[valueShift];
        byte[] result = new byte[array.length];

        System.arraycopy(array, 0, leftPart, 0, array.length - valueShift);
        System.arraycopy(array, array.length - valueShift, rightPart, 0, valueShift);
        System.arraycopy(rightPart, 0, result, 0, valueShift);
        System.arraycopy(leftPart, 0, result, valueShift, array.length - valueShift);

        return result;
    }

    public byte[][] generateRoundKey(byte[][] prevRoundKey) {
        byte[] Rcon = new byte[prevRoundKey.length];
        byte[][] roundKey = new byte[prevRoundKey.length][prevRoundKey[0].length];

        byte[][] prevAndCurrentKey = new byte[prevRoundKey.length][prevRoundKey[0].length * 2];

        for (int i = 0; i < prevRoundKey.length; i++) {
            System.arraycopy(prevRoundKey[i], 0, prevAndCurrentKey[i], 0, prevRoundKey[i].length);
        }

        for (int i = prevRoundKey.length; i < prevAndCurrentKey[0].length; i++) {
            byte[] column = new byte[] {prevRoundKey[0][i - 1], prevRoundKey[1][i - 1], prevRoundKey[2][i - 1], prevRoundKey[3][i - 1]};
            byte[] shiftColumn = shiftLeft(column, 1);
            byte[] subColumn = subBytesArray(shiftColumn);
            byte[] temp = new byte[prevRoundKey.length];

            for (int j = 0; j < prevRoundKey.length; j++) {
                temp[j] = (byte) (prevRoundKey[j][i - 4] ^ subColumn[j] ^ Rcon[j]);
            }

            prevAndCurrentKey[0][i] = temp[0];
            prevAndCurrentKey[1][i] = temp[1];
            prevAndCurrentKey[2][i] = temp[2];
            prevAndCurrentKey[3][i] = temp[3];
        }

        for (int i = 0; i < prevRoundKey.length; i++) {
            System.arraycopy(prevAndCurrentKey[i], prevRoundKey.length, roundKey[i], 0, prevRoundKey[0].length);
        }

        return roundKey;
    }
}
