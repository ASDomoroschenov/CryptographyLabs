package ru.mai.encryption.encryption_impl.DES;

import ru.mai.encryption.encryption_interface.IRoundKeyGenerator;
import ru.mai.utils.BitsUtil;
import ru.mai.utils.BytesUtil;

public class DESReverseRoundKeyGenerator implements IRoundKeyGenerator {
    private static final int SIZE_KEY_BITS = 56;

    private static final int[] COMPRESS_KEY_PERMUTATION = {
            14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    };

    private static final int[] SHIFT_ROUND = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private static final int NUM_ROUNDS = 16;

    @Override
    public byte[][] generate(byte[] keyBytes) {
        long key = BytesUtil.bytesToLong(keyBytes);
        long leftPartKey = key >>> (Long.SIZE - SIZE_KEY_BITS / 2);
        long rightPartKey = (key << (SIZE_KEY_BITS / 2)) >>> (Long.SIZE - SIZE_KEY_BITS / 2);
        long oneShiftLeftPartKey = BitsUtil.cyclicLeftShift(leftPartKey, SIZE_KEY_BITS / 2, 1);
        long oneShiftRightPartKey = BitsUtil.cyclicLeftShift(rightPartKey, SIZE_KEY_BITS / 2, 1);
        long twoShiftLeftPartKey = BitsUtil.cyclicLeftShift(leftPartKey, SIZE_KEY_BITS / 2, 2);
        long twoShiftRightPartKey = BitsUtil.cyclicLeftShift(rightPartKey, SIZE_KEY_BITS / 2, 2);
        byte[] oneShiftKey = BytesUtil.permutation(BytesUtil.longToBytes((oneShiftLeftPartKey << SIZE_KEY_BITS / 2) | oneShiftRightPartKey, SIZE_KEY_BITS / Byte.SIZE), COMPRESS_KEY_PERMUTATION);
        byte[] towShiftKey = BytesUtil.permutation(BytesUtil.longToBytes((twoShiftLeftPartKey << SIZE_KEY_BITS / 2) | twoShiftRightPartKey, SIZE_KEY_BITS / Byte.SIZE), COMPRESS_KEY_PERMUTATION);
        byte[][] result = new byte[NUM_ROUNDS][];

        for (int i = 0; i < NUM_ROUNDS; i++) {
            if (SHIFT_ROUND[NUM_ROUNDS - i - 1] == 1) {
                result[i] = oneShiftKey;
            } else {
                result[i] = towShiftKey;
            }
        }

        return result;
    }
}
