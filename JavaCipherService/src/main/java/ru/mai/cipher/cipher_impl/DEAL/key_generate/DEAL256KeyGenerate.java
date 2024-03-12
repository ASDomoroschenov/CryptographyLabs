package ru.mai.cipher.cipher_impl.DEAL.key_generate;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_impl.DES.DES;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.utils_impl.BytesUtil;

@AllArgsConstructor
public class DEAL256KeyGenerate {
    private static final byte[] KEY_DES = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final byte[][] SHIFTS_BIT = {
            {0, 0, 0, 0, 0, 0, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 4},
            {0, 0, 0, 0, 0, 0, 0, 16},
            {0, 0, 0, 0, 0, 0, 1, 0}
    };
    private static final int SIZE_PART_KEY = 8;
    private static final int NUMBER_OF_ROUNDS = 8;
    private byte[] key;

    public byte[][] generate() {
        ICipher desCipher = new DES(KEY_DES);
        byte[] key1 = new byte[SIZE_PART_KEY];
        byte[] key2 = new byte[SIZE_PART_KEY];
        byte[] key3 = new byte[SIZE_PART_KEY];
        byte[] key4 = new byte[SIZE_PART_KEY];

        System.arraycopy(key, 0, key1, 0, SIZE_PART_KEY);
        System.arraycopy(key, SIZE_PART_KEY, key2, 0, SIZE_PART_KEY);
        System.arraycopy(key, SIZE_PART_KEY * 2, key3, 0, SIZE_PART_KEY);
        System.arraycopy(key, SIZE_PART_KEY * 3, key4, 0, SIZE_PART_KEY);

        byte[][] keys = new byte[NUMBER_OF_ROUNDS][SIZE_PART_KEY];

        keys[0] = desCipher.encrypt(key1);
        keys[1] = desCipher.encrypt(BytesUtil.xor(key2, keys[0]));
        keys[2] = desCipher.encrypt(BytesUtil.xor(key3, keys[1]));
        keys[3] = desCipher.encrypt(BytesUtil.xor(key4, keys[2]));
        keys[4] = desCipher.encrypt(BytesUtil.xor(BytesUtil.xor(key1, SHIFTS_BIT[0]), keys[3]));
        keys[5] = desCipher.encrypt(BytesUtil.xor(BytesUtil.xor(key2, SHIFTS_BIT[1]), keys[4]));
        keys[6] = desCipher.encrypt(BytesUtil.xor(BytesUtil.xor(key3, SHIFTS_BIT[2]), keys[5]));
        keys[7] = desCipher.encrypt(BytesUtil.xor(BytesUtil.xor(key4, SHIFTS_BIT[3]), keys[6]));

        return keys;
    }
}
