package ru.mai.cipher.cipher_impl.DEAL;

import ru.mai.cipher.cipher_impl.DEAL.cipher_conversion.DEALCipherConversion;
import ru.mai.cipher.cipher_impl.DEAL.key_generate.DEALDecryptKeyGenerator;
import ru.mai.cipher.cipher_impl.DEAL.key_generate.DEALEncryptKeyGenerator;
import ru.mai.cipher.cipher_impl.DEAL.key_generate.DEALKeyGenerate;
import ru.mai.cipher.cipher_impl.feistel_network.FeistelNetwork;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.utils_impl.BytesUtil;

public class DEAL implements ICipher {
    private static final int TEXT_BLOCK_BYTES_SIZE = 16;
    private static final int KEY_SIZE_128 = 16;
    private static final int KEY_SIZE_192 = 24;
    private static final int KEY_SIZE_256 = 32;
    private byte[] key;
    private DEALKeyGenerate keyGenerator;

    public DEAL(byte[] key) {
        this.key = key;
        this.keyGenerator = new DEALKeyGenerate(key);
    }

    @Override
    public byte[] encrypt(byte[] bytes) throws IllegalArgumentException {
        byte[][] halfParts = BytesUtil.splitInHalf(bytes);
        byte[] swapParts = BytesUtil.mergePart(halfParts[1], halfParts[0]);
        return new FeistelNetwork(new DEALEncryptKeyGenerator(keyGenerator), new DEALCipherConversion()).apply(swapParts, key, keyGenerator.getNumRounds());
    }

    @Override
    public byte[] decrypt(byte[] bytes) throws IllegalArgumentException {
        byte[] result = new FeistelNetwork(new DEALDecryptKeyGenerator(keyGenerator), new DEALCipherConversion()).apply(bytes, key, keyGenerator.getNumRounds());
        byte[][] halfParts = BytesUtil.splitInHalf(result);
        return BytesUtil.mergePart(halfParts[1], halfParts[0]);
    }

    @Override
    public int getTextBlockSize() {
        return TEXT_BLOCK_BYTES_SIZE;
    }

    @Override
    public boolean checkKey(byte[] key) {
        return key.length == KEY_SIZE_128 || key.length == KEY_SIZE_192 || key.length == KEY_SIZE_256;
    }
}
