package ru.mai.rijndael;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Map;

public class Rijndael {
    private static final Map<Pair<Integer, Integer>, Integer> MAP_ROUNDS = Map.of(
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
    private final byte[] sBox;
    private final byte[] invertSBox;
    private final KeyGenerator keyGenerator;
    private final int countBitsBlock;

    public Rijndael(byte[] key, int countBitsBlock, int countBitsKey, char irreduciblePolynomial) {
        this.countRounds = MAP_ROUNDS.get(Pair.of(countBitsBlock, countBitsKey));
        this.modulo = irreduciblePolynomial;
        this.sBox = generateSBox();
        this.invertSBox = generateInvertSBox(this.sBox);
        this.keyGenerator = new KeyGenerator(key, countBitsKey, countBitsBlock, countRounds);
        this.countBitsBlock = countBitsBlock;
    }

    public byte[] encrypt(byte[] text) {
        return null;
    }

    public byte[] decrypt(byte[] text) {
        return null;
    }

    public byte[] encryptBlock(byte[] text) {
        byte[][] state = convertArrayToMatrix(text, 4, countBitsBlock / 32);
        state = addRoundKey(state, keyGenerator.getRoundKey(0));

        for (int i = 1; i < countRounds; i++) {
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, keyGenerator.getRoundKey(i));
        }

        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, keyGenerator.getRoundKey(countRounds));

        return convertMatrixToArray(state);
    }

    public byte[] decryptBlock(byte[] text) {
        byte[][] state = convertArrayToMatrix(text, 4, countBitsBlock / 32);

        state = addRoundKey(state, keyGenerator.getRoundKey(countRounds));
        state = invShiftRows(state);
        state = invSubBytes(state);

        for (int i = countRounds - 1; i >= 1; i--) {
            state = addRoundKey(state, keyGenerator.getRoundKey(i));
            state = invMixColumns(state);
            state = invShiftRows(state);
            state = invSubBytes(state);
        }

        state = addRoundKey(state, keyGenerator.getRoundKey(0));

        return convertMatrixToArray(state);
    }

    public byte[][] convertArrayToMatrix(byte[] array, int countRows, int countColumns) {
        byte[][] matrix = new byte[countRows][countColumns];

        for (int i = 0; i < countRows; i++) {
            System.arraycopy(array, i * countColumns, matrix[i], 0, countColumns);
        }

        return matrix;
    }

    public byte[] convertMatrixToArray(byte[][] matrix) {
        byte[] result = new byte[matrix.length * matrix[0].length];
        int index = 0;

        for (byte[] row : matrix) {
            for (byte itemRow : row) {
                result[index++] = itemRow;
            }
        }

        return result;
    }

    public byte[][] subBytes(byte[][] state) {
        return subBytesWrap(state, sBox);
    }

    public byte[][] invSubBytes(byte[][] state) {
        return subBytesWrap(state, invertSBox);
    }

    public byte[][] subBytesWrap(byte[][] state, byte[] sArray) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int indexSBox = state[i][j] < 0 ? 256 + state[i][j] : state[i][j];
                result[i][j] = sArray[indexSBox];
            }
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

    public byte[][] invShiftRows(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            result[i] = shiftRight(state[i], i);
        }

        return result;
    }

    public byte[][] mixColumns(byte[][] state) {
        byte[][] matrix = new byte[][]{
                {0x02, 0x03, 0x01, 0x01},
                {0x01, 0x02, 0x03, 0x01},
                {0x01, 0x01, 0x02, 0x03},
                {0x03, 0x01, 0x01, 0x02}
        };

        return mixColumnsWrap(state, matrix);
    }

    public byte[][] invMixColumns(byte[][] state) {
        byte[][] matrix = new byte[][]{
                {0x0e, 0x0b, 0x0d, 0x09},
                {0x09, 0x0e, 0x0b, 0x0d},
                {0x0d, 0x09, 0x0e, 0x0b},
                {0x0b, 0x0d, 0x09, 0x0e}
        };

        return mixColumnsWrap(state, matrix);
    }

    private byte[][] mixColumnsWrap(byte[][] state, byte[][] matrix) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int col = 0; col < state[0].length; col++) {
            byte[] stateColumn = new byte[]{state[0][col], state[1][col], state[2][col], state[3][col]};
            byte[] resultMultiplication = multiplicationPolynomials(stateColumn, matrix);
            result[0][col] = resultMultiplication[0];
            result[1][col] = resultMultiplication[1];
            result[2][col] = resultMultiplication[2];
            result[3][col] = resultMultiplication[3];
        }

        return result;
    }

    private byte[] multiplicationPolynomials(byte[] column, byte[][] matrix) {
        int sizePolynomial = column.length;
        byte[] result = new byte[sizePolynomial];

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

    public byte[] generateSBox() {
        byte[] aMatrix = new byte[]{
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
        byte[] result = new byte[256];

        for (int i = 0; i < 256; i++) {
            result[i] = (byte) (multiplyMatrixToVector(aMatrix, GF.invert((byte) i, modulo)) ^ f);
        }

        return result;
    }

    public byte[] generateInvertSBox(byte[] array) {
        byte[] result = new byte[256];

        for (int i = 0; i < 256; i++) {
            int index = array[i] < 0 ? 256 + array[i] : array[i];
            result[index] = (byte) i;
        }

        return result;
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

    public class KeyGenerator {
        private final byte[][] key;
        private final int sizeKeyBits;
        private final int sizeBlockBits;
        private final int countRounds;
        private final byte[][] keyExpanded;

        public KeyGenerator(byte[] key, int sizeKeyBits, int sizeBlockBits, int countRounds) {
            this.key = convertArrayToMatrix(key, 4, sizeKeyBits / 32);
            this.sizeKeyBits = sizeKeyBits;
            this.sizeBlockBits = sizeBlockBits;
            this.countRounds = countRounds;
            this.keyExpanded = keyExpansion();
        }

        public byte getRConstant(int index) {
            if (index == 0) {
                return 0x00;
            }
            if (index == 1) {
                return 0x01;
            }
            if (index == 2) {
                return 0x02;
            }

            return GF.multiplicationModulo((byte) 0x02, getRConstant(index - 1), modulo);
        }

        public byte[][] keyExpansion() {
            int indexSBox;
            int sizeColumnBlock = sizeBlockBits / 32;
            int sizeColumnKey = sizeKeyBits / 32;
            byte[][] result = new byte[4][sizeColumnBlock * (countRounds + 1)];

            for (int i = 0; i < 4; i++) {
                System.arraycopy(key[i], 0, result[i], 0, key[i].length);
            }

            for (int j = sizeColumnKey; j < sizeColumnBlock * (countRounds + 1); j++) {
                if (j % sizeColumnKey == 0) {
                    indexSBox = result[1][j - 1] < 0 ?
                                256 + result[1][j - 1] :
                                result[1][j - 1];

                    result[0][j] = (byte) (result[0][j - sizeColumnKey] ^ sBox[indexSBox] ^ getRConstant(j / sizeColumnKey));

                    for (int i = 1; i < 4; i++) {
                        indexSBox = result[(i + 1) % 4][j - 1] < 0 ?
                                    256 + result[(i + 1) % 4][j - 1] :
                                    result[(i + 1) % 4][j - 1];

                        result[i][j] = (byte) (result[i][j - sizeColumnKey] ^ sBox[indexSBox]);
                    }
                } else if (sizeColumnKey > 6) {
                    if (j % sizeColumnKey == 4) {
                        for (int i = 0; i < 4; i++) {
                            indexSBox = result[i][j - 1] < 0 ?
                                        256 + result[i][j - 1] :
                                        result[i][j - 1];

                            result[i][j] = (byte) (result[i][j - sizeColumnKey] ^ sBox[indexSBox]);
                        }
                    } else {
                        for (int i = 0; i < 4; i++) {
                            result[i][j] = (byte) (result[i][j - sizeColumnKey] ^ result[i][j - 1]);
                        }
                    }
                } else {
                    for (int i = 0; i < 4; i++) {
                        result[i][j] = (byte) (result[i][j - sizeColumnKey] ^ result[i][j - 1]);
                    }
                }
            }

            return result;
        }

        public byte[][] getRoundKey(int indexRound) {
            int sizeColumnBlock = sizeBlockBits / 32;
            byte[][] result = new byte[4][sizeColumnBlock];

            for (int i = 0; i < 4; i++) {
                System.arraycopy(keyExpanded[i], sizeColumnBlock * indexRound, result[i], 0, sizeColumnBlock);
            }

            return result;
        }
    }

    public void printMatrix(byte[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(String.format("0x%x ", matrix[i][j]));
            }
            System.out.println();
        }
    }
}