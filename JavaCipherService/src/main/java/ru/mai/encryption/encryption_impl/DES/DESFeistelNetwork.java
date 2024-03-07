package ru.mai.encryption.encryption_impl.DES;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.IEncryption;
import ru.mai.encryption.encryption_interface.IFeistelNetwork;
import ru.mai.encryption.encryption_interface.IRoundKeyGenerator;
import ru.mai.utils.BytesUtil;

@AllArgsConstructor
public class DESFeistelNetwork implements IFeistelNetwork {
    private IRoundKeyGenerator keyGenerator;
    private IEncryption encryption;

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
