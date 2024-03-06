package ru.mai.encryption.encryption_impl.DES;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_interface.Encryption;
import ru.mai.encryption.encryption_interface.FeistelNetwork;
import ru.mai.encryption.encryption_interface.RoundKeyGenerator;
import ru.mai.utils.BitsUtil;

@AllArgsConstructor
public class DESFeistelNetwork implements FeistelNetwork {
    private RoundKeyGenerator keyGenerator;
    private Encryption encryption;

    @Override
    public byte[] apply(byte[] bytes, byte[] key, int numRounds) {
        byte[] result = new byte[bytes.length];
        byte[] leftPart = new byte[bytes.length / 2];
        byte[] rightPart = new byte[bytes.length / 2];

        System.arraycopy(bytes, 0, leftPart, 0, bytes.length / 2);
        System.arraycopy(bytes, bytes.length / 2, rightPart, 0, bytes.length / 2);

        for (int i = 0; i < numRounds; i++) {
            byte[] roundKey = keyGenerator.generateKeyRound(key);
            byte[] temp = leftPart;
            leftPart = BitsUtil.xor(rightPart, encryption.encrypt(leftPart, roundKey));
            rightPart = temp;
        }

        System.arraycopy(bytes, 0, result, 0, bytes.length / 2);
        System.arraycopy(bytes, bytes.length / 2, result, bytes.length / 2, bytes.length / 2);

        return result;
    }
}
