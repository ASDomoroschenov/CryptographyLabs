package ru.mai.RSA.stream_mode.OFB;

import ru.mai.RSA.RSA;
import ru.mai.RSA.padding.IPadding;
import ru.mai.RSA.padding.padding_impl.ANSIX923Padding;
import ru.mai.RSA.stream_mode.IStreamCipher;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;
import ru.mai.utils.utils_impl.BytesUtil;

import java.math.BigInteger;

public class OFBMode implements IStreamCipher {
    private final RSA rsa;
    private final BigInteger initialVector;
    private final BigInteger e;
    private final BigInteger N;
    private final IPadding padding;
    private final int sizeBlock;

    public OFBMode(RSA rsa) {
        this.rsa = rsa;
        BigIntegerRandomGenerator random = new BigIntegerRandomGenerator();
        BigInteger[][] keys = rsa.getKey();
        this.e = keys[0][0];
        this.N = keys[1][1];
        this.padding = new ANSIX923Padding();
        this.initialVector = random.generateInBounds(BigInteger.TWO, N.subtract(BigInteger.ONE));
        this.sizeBlock = initialVector.toByteArray().length;
    }

    @Override
    public byte[] encrypt(byte[] text) {
        if (e.compareTo(BigInteger.valueOf(sizeBlock)) < 0) {
            throw new IllegalArgumentException("Message is subject to Hastad attack");
        } else {
            text = padding.addPAdding(text, sizeBlock);
            byte[] result = new byte[text.length];
            byte[] textBlock = new byte[sizeBlock];
            byte[] key = initialVector.toByteArray();

            for (int i = 0; i < text.length; i+= sizeBlock) {
                System.arraycopy(text, i, textBlock, 0, sizeBlock);
                byte[] cipherBlockText = BytesUtil.xor(textBlock, key);
                System.arraycopy(cipherBlockText, 0, result, i, cipherBlockText.length);
                key = rsa.cipherConversionBlock(key, e, N, sizeBlock);
            }

            return result;
        }
    }

    @Override
    public byte[] decrypt(byte[] text) {
        if (e.compareTo(BigInteger.valueOf(sizeBlock)) < 0) {
            throw new IllegalArgumentException("Message is subject to Hastad attack");
        } else {
            byte[] result = new byte[text.length];
            byte[] textBlock = new byte[sizeBlock];
            byte[] key = initialVector.toByteArray();

            for (int i = 0; i < text.length; i+= sizeBlock) {
                System.arraycopy(text, i, textBlock, 0, sizeBlock);
                byte[] cipherBlockText = BytesUtil.xor(textBlock, key);
                System.arraycopy(cipherBlockText, 0, result, i, cipherBlockText.length);
                key = rsa.cipherConversionBlock(key, e, N, sizeBlock);
            }

            return padding.removePadding(result);
        }
    }
}
