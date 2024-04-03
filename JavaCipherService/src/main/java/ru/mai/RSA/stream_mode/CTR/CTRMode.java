package ru.mai.RSA.stream_mode.CTR;

import ru.mai.RSA.RSA;
import ru.mai.RSA.padding.IPadding;
import ru.mai.RSA.padding.padding_impl.ANSIX923Padding;
import ru.mai.RSA.stream_mode.IStreamCipher;
import ru.mai.utils.utils_impl.BigIntegerRandomGenerator;
import ru.mai.utils.utils_impl.BytesUtil;

import java.math.BigInteger;
import java.util.Arrays;

public class CTRMode implements IStreamCipher {
    private final RSA rsa;
    private final BigInteger initialVector;
    private final BigInteger e;
    private final BigInteger N;
    private final IPadding padding;
    private final int sizeBlock;

    public CTRMode(RSA rsa) {
        this.rsa = rsa;
        BigIntegerRandomGenerator random = new BigIntegerRandomGenerator();
        BigInteger[][] keys = rsa.getKey();
        this.e = keys[0][0];
        this.N = keys[1][1];
        this.padding = new ANSIX923Padding();
        this.initialVector = random.generateInBounds(BigInteger.TWO, N.subtract(BigInteger.ONE));
        this.sizeBlock = initialVector.toByteArray().length;
    }

    private byte[] getNextCounter(byte[] counter) {
        byte[][] halfParts = BytesUtil.splitInHalf(counter);
        byte[] moduleBytes = new byte[halfParts[1].length];
        int sizeRightPart = halfParts[1].length;

        Arrays.fill(moduleBytes, (byte) 127);
        BigInteger rightPartNumber = new BigInteger(halfParts[1]);
        BigInteger module = new BigInteger(moduleBytes);

        rightPartNumber = rightPartNumber.add(BigInteger.ONE).mod(module);

        byte[] newRightPart = rightPartNumber.toByteArray();

        if (newRightPart.length != sizeRightPart) {
            byte[] temp = new byte[sizeRightPart];
            System.arraycopy(temp, sizeRightPart - newRightPart.length, newRightPart, 0, newRightPart.length);
            newRightPart = temp;
        }

        return BytesUtil.mergePart(halfParts[0], newRightPart);
    }

    @Override
    public byte[] encrypt(byte[] text) {
        if (e.compareTo(BigInteger.valueOf(sizeBlock)) < 0) {
            throw new IllegalArgumentException("Message is subject to Hastad attack");
        } else {
            text = padding.addPAdding(text, sizeBlock);
            byte[] result = new byte[text.length];
            byte[] textBlock = new byte[sizeBlock];
            byte[] counter = initialVector.toByteArray();

            for (int i = 0; i < text.length; i += sizeBlock) {
                System.arraycopy(text, i, textBlock, 0, sizeBlock);
                byte[] cipherBlockText = BytesUtil.xor(textBlock, rsa.cipherConversionBlock(counter, e, N, sizeBlock));
                System.arraycopy(cipherBlockText, 0, result, i, cipherBlockText.length);
                counter = getNextCounter(counter);
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
            byte[] counter = initialVector.toByteArray();

            for (int i = 0; i < text.length; i += sizeBlock) {
                System.arraycopy(text, i, textBlock, 0, sizeBlock);
                byte[] cipherBlockText = BytesUtil.xor(textBlock, rsa.cipherConversionBlock(counter, e, N, sizeBlock));
                System.arraycopy(cipherBlockText, 0, result, i, cipherBlockText.length);
                counter = getNextCounter(counter);
            }

            return padding.removePadding(result);
        }
    }
}
