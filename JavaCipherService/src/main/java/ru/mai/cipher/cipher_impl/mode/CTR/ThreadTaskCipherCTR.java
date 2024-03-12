package ru.mai.cipher.cipher_impl.mode.CTR;

import ru.mai.utils.utils_impl.PairIndexText;
import ru.mai.utils.utils_interface.IThreadTask;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.utils_impl.BytesUtil;

public class ThreadTaskCipherCTR implements IThreadTask {
    private final ICipher cipher;
    private final byte[][] counterBlocks;

    public ThreadTaskCipherCTR(ICipher cipher, byte[] text, byte[] initialVector) {
        this.cipher = cipher;
        counterBlocks = new byte[text.length / cipher.getTextBlockSize()][cipher.getTextBlockSize()];
        byte[] counter = initialVector.clone();

        for (int i = 0; i < text.length / cipher.getTextBlockSize(); i++) {
            counterBlocks[i] = counter;
            counter = getNextCounter(counter);
        }
    }

    private byte[] getNextCounter(byte[] counter) {
        byte[][] halfParts = BytesUtil.splitInHalf(counter);
        byte[] rightPartCounter = BytesUtil.longToBytes(BytesUtil.bytesToLong(halfParts[1]) + 1, Long.BYTES);
        return BytesUtil.mergePart(halfParts[0], rightPartCounter);
    }

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = BytesUtil.xor(textBlock, cipher.encrypt(counterBlocks[(indexBegin + i * textBlockSize) / textBlockSize]));
            System.arraycopy(cipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairIndexText(indexBegin, result);
    }
}
