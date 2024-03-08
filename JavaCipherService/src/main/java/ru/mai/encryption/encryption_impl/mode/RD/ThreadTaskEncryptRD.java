package ru.mai.encryption.encryption_impl.mode.RD;

import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.PairMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.encryption.encryption_interface.ICipher;
import ru.mai.utils.BytesUtil;

import java.util.Arrays;

public class ThreadTaskEncryptRD implements IThreadTask {
    ICipher cipher;
    byte[][] counterBlocks;

    public ThreadTaskEncryptRD(ICipher cipher, byte[] text, byte[] initialVector) {
        this.cipher = cipher;
        counterBlocks = new byte[text.length / cipher.getTextBlockSize()][cipher.getTextBlockSize()];
        long delta = BytesUtil.bytesToLong(initialVector) << Integer.SIZE >> Integer.SIZE;
        long counter = BytesUtil.bytesToLong(cipher.encrypt(initialVector));

        for (int i = 0; i < text.length / cipher.getTextBlockSize(); i++) {
            counterBlocks[i] = BytesUtil.longToBytes(counter, Long.BYTES);
            counter = getNextCounter(counter, delta);
        }
    }

    private long getNextCounter(long counter, long delta) {
        return counter + delta;
    }

    @Override
    public PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = cipher.encrypt(BytesUtil.xor(textBlock, counterBlocks[(indexBegin + i * textBlockSize) / textBlockSize]));
            System.arraycopy(cipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairMode(indexBegin, result);
    }
}
