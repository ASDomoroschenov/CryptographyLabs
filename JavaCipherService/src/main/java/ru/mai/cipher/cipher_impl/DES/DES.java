package ru.mai.cipher.cipher_impl.DES;

import ru.mai.cipher.cipher_impl.DES.cipher_conversion.DESCipherConversion;
import ru.mai.cipher.cipher_impl.feistel_network.FeistelNetwork;
import ru.mai.cipher.cipher_impl.DES.key_generate.DESDecryptKeyGenerator;
import ru.mai.cipher.cipher_impl.DES.key_generate.DESEncryptKeyGenerator;
import ru.mai.cipher.cipher_impl.DES.key_generate.DESKeyGenerator;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.IFeistelNetwork;
import ru.mai.utils.utils_impl.BytesUtil;

public class DES implements ICipher {
    private final byte[] key;
    private final DESKeyGenerator keyGenerator;

    private static final int NUMBER_OF_ROUNDS = 16;

    private static final int TEXT_BLOCK_BYTES_SIZE = 8;

    private static final int KEY_BYTES_SIZE = 8;

    private static final int[] INITIAL_PERMUTATION = {
            58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] FINAL_PERMUTATION = {
            40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
    };

    public DES(byte[] key) {
        if (key == null || key.length != KEY_BYTES_SIZE) {
            throw new IllegalArgumentException("Invalid key");
        }

        this.key = key;
        keyGenerator = new DESKeyGenerator(key);
    }

    @Override
    public byte[] encrypt(byte[] block) throws IllegalArgumentException {
        if (block == null || block.length != TEXT_BLOCK_BYTES_SIZE) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        IFeistelNetwork feistelNetwork = new FeistelNetwork(new DESEncryptKeyGenerator(keyGenerator), new DESCipherConversion());
        block = BytesUtil.permutation(block, INITIAL_PERMUTATION);
        block = feistelNetwork.apply(block, key, NUMBER_OF_ROUNDS);
        return BytesUtil.permutation(block, FINAL_PERMUTATION);
    }

    @Override
    public byte[] decrypt(byte[] block) throws IllegalArgumentException {
        if (block == null || block.length != TEXT_BLOCK_BYTES_SIZE) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        IFeistelNetwork feistelNetwork = new FeistelNetwork(new DESDecryptKeyGenerator(keyGenerator), new DESCipherConversion());
        block = BytesUtil.permutation(block, INITIAL_PERMUTATION);
        block = feistelNetwork.apply(block, key, NUMBER_OF_ROUNDS);
        return BytesUtil.permutation(block, FINAL_PERMUTATION);
    }

    @Override
    public int getTextBlockSize() {
        return TEXT_BLOCK_BYTES_SIZE;
    }

    @Override
    public boolean checkKey(byte[] key) {
        return key.length == KEY_BYTES_SIZE;
    }
}
