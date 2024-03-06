package ru.mai.encryption.encryption_impl.DES;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.Encryption;
import ru.mai.encryption.encryption_interface.FeistelNetwork;
import ru.mai.encryption.encryption_interface.RoundKeyGenerator;
import ru.mai.utils.BytesUtil;

@AllArgsConstructor
public class DESFeistelNetwork implements FeistelNetwork {
    private RoundKeyGenerator keyGenerator;
    private Encryption encryption;

    @Override
    public byte[] apply(byte[] bytes, byte[] key, int numRounds) {
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