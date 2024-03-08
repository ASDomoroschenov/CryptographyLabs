package ru.mai.encryption.cipher_impl.mode.CTR;

import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.PairMode;
import ru.mai.encryption.cipher_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.encryption.cipher_interface.ICipher;
import ru.mai.utils.BytesUtil;

public class ThreadTaskCTR implements IThreadTask {
    private final ICipher cipher;
    private final byte[][] counterBlocks;

    public ThreadTaskCTR(ICipher cipher, byte[] text, byte[] initialVector) {
        this.cipher = cipher;
        counterBlocks = new byte[text.length / cipher.getTextBlockSize()][cipher.getTextBlockSize()];
        long counter = BytesUtil.bytesToLong(initialVector);

        for (int i = 0; i < text.length / cipher.getTextBlockSize(); i++) {
            counterBlocks[i] = BytesUtil.longToBytes(counter, Long.BYTES);
            counter = getNextCounter(counter);
        }
    }

    private long getNextCounter(long counter) {
        int rightPartCounter = ((int) ((counter << Integer.SIZE) >> Integer.SIZE)) + 1;
        long leftPartCounter = counter >> Integer.SIZE;
        return leftPartCounter << Integer.SIZE | rightPartCounter;
    }

    @Override
    public PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = BytesUtil.xor(textBlock, cipher.encrypt(counterBlocks[(indexBegin + i * textBlockSize) / textBlockSize]));
            System.arraycopy(cipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairMode(indexBegin, result);
    }
}
