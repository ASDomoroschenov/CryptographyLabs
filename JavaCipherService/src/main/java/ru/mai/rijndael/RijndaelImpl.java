package ru.mai.rijndael;

import org.apache.commons.lang3.tuple.Pair;
import ru.mai.rijndael.padding.padding_impl.ANSIX923Padding;
import ru.mai.rijndael.padding.padding_impl.ISO10126Padding;
import ru.mai.rijndael.padding.padding_impl.PKCS7Padding;
import ru.mai.rijndael.padding.padding_impl.ZerosPadding;
import ru.mai.rijndael.padding.padding_interface.IPadding;
import ru.mai.rijndael.utils.GF;

import java.util.Map;

public class RijndaelImpl extends Rijndael {
    private final int countBitsKey;
    private final char modulo;
    private final byte[] sBox;
    private final byte[] invertSBox;
    private final byte[][] keyExpanded;

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

    public RijndaelImpl(byte[] key, int countBitsBlock, int countBitsKey, char modulo, PaddingMode mode) {
        super.countBitsBlock = countBitsBlock;
        super.key = convertArrayToMatrix(key, 4, countBitsKey / 32);
        super.countRounds = MAP_ROUNDS.get(Pair.of(countBitsBlock, countBitsKey));
        super.padding = getPadding(mode);

        this.countBitsKey = countBitsKey;
        this.modulo = modulo;
        this.sBox = generateSBox();
        this.invertSBox = generateInvertSBox(this.sBox);
        this.keyExpanded = new KeyGenerator(key).keyExpansion();
    }

    private IPadding getPadding(PaddingMode mode) {
        return switch (mode) {
            case ZEROS -> new ZerosPadding();
            case ANSI_X_923 -> new ANSIX923Padding();
            case PKCS7 -> new PKCS7Padding();
            case ISO_10126 -> new ISO10126Padding();
        };
    }

    @Override
    public byte[][] convertArrayToMatrix(byte[] array, int countRows, int countColumns) {
        byte[][] matrix = new byte[countRows][countColumns];

        for (int i = 0; i < countRows; i++) {
            System.arraycopy(array, i * countColumns, matrix[i], 0, countColumns);
        }

        return matrix;
    }

    @Override
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

    @Override
    public byte[][] subBytes(byte[][] state) {
        return subBytesWrap(state, sBox);
    }

    @Override
    public byte[][] shiftRows(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            result[i] = shiftLeft(state[i], i);
        }

        return result;
    }

    @Override
    public byte[][] mixColumns(byte[][] state) {
        byte[][] matrix = new byte[][]{
                {0x02, 0x03, 0x01, 0x01},
                {0x01, 0x02, 0x03, 0x01},
                {0x01, 0x01, 0x02, 0x03},
                {0x03, 0x01, 0x01, 0x02}
        };

        return mixColumnsWrap(state, matrix);
    }

    @Override
    public byte[][] invSubBytes(byte[][] state) {
        return subBytesWrap(state, invertSBox);
    }

    @Override
    public byte[][] invShiftRows(byte[][] state) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            result[i] = shiftRight(state[i], i);
        }

        return result;
    }

    @Override
    public byte[][] invMixColumns(byte[][] state) {
        byte[][] matrix = new byte[][]{
                {0x0e, 0x0b, 0x0d, 0x09},
                {0x09, 0x0e, 0x0b, 0x0d},
                {0x0d, 0x09, 0x0e, 0x0b},
                {0x0b, 0x0d, 0x09, 0x0e}
        };

        return mixColumnsWrap(state, matrix);
    }

    @Override
    public byte[][] addRoundKey(byte[][] state, byte[][] roundKey) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                result[i][j] = (byte) (state[i][j] ^ roundKey[i][j]);
            }
        }

        return result;
    }

    @Override
    public byte[][] getRoundKey(int numRound) {
        int sizeColumnBlock = countBitsBlock / 32;
        byte[][] result = new byte[4][sizeColumnBlock];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(keyExpanded[i], sizeColumnBlock * numRound, result[i], 0, sizeColumnBlock);
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

    private byte[][] mixColumnsWrap(byte[][] state, byte[][] matrix) {
        byte[][] result = new byte[state.length][state[0].length];

        for (int col = 0; col < state[0].length; col++) {
            byte[] stateColumn = new byte[]{state[0][col], state[1][col], state[2][col], state[3][col]};
            byte[] resultMultiplication = multiplicationPolynomials(stateColumn, matrix);

            for (int i = 0; i < 4; i++) {
                result[i][col] = resultMultiplication[i];
            }
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

        public KeyGenerator(byte[] key) {
            this.key = convertArrayToMatrix(key, 4, countBitsKey / 32);
        }

        public byte getRConstant(int index) {
            if (index < 3) {
                return (byte) index;
            }

            return GF.multiplicationModulo((byte) 0x02, getRConstant(index - 1), modulo);
        }

        public byte[][] keyExpansion() {
            int indexSBox;
            int sizeColumnBlock = countBitsBlock / 32;
            int sizeColumnKey = countBitsKey / 32;
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
    }
}
