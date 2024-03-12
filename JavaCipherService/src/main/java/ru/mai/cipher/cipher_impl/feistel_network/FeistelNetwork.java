package ru.mai.cipher.cipher_impl.feistel_network;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_interface.ICipherConversion;
import ru.mai.cipher.cipher_interface.IFeistelNetwork;
import ru.mai.cipher.cipher_interface.IRoundKeyGenerator;
import ru.mai.utils.utils_impl.BytesUtil;

@AllArgsConstructor
public class FeistelNetwork implements IFeistelNetwork {
    private IRoundKeyGenerator keyGenerator;
    private ICipherConversion encryption;

    @Override
    public byte[] apply(byte[] bytes, byte[] key, int numRounds) throws IllegalArgumentException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Illegal bytes key");
        }
        if (numRounds <= 0) {
            throw new IllegalArgumentException("Illegal number of rounds");
        }

        byte[][] splitHalfBytes = BytesUtil.splitInHalf(bytes);
        byte[][] roundKeys = keyGenerator.generate(key);

        for (int i = 0; i < numRounds - 1; i++) {
            byte[] temp = splitHalfBytes[1];
            splitHalfBytes[1] = BytesUtil.xor(splitHalfBytes[0], encryption.apply(splitHalfBytes[1], roundKeys[i]));
            splitHalfBytes[0] = temp;
        }

        return BytesUtil.mergePart(BytesUtil.xor(splitHalfBytes[0], encryption.apply(splitHalfBytes[1], roundKeys[numRounds - 1])), splitHalfBytes[1]);
    }
}
