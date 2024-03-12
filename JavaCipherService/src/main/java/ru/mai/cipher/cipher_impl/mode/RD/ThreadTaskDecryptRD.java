package ru.mai.cipher.cipher_impl.mode.RD;

import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.utils_impl.BytesUtil;
import ru.mai.utils.utils_impl.PairIndexText;
import ru.mai.utils.utils_interface.IThreadTask;

public class ThreadTaskDecryptRD implements IThreadTask {
    ICipher cipher;
    byte[][] counterBlocks;

    public ThreadTaskDecryptRD(ICipher cipher, byte[] text, byte[] initialVector) {
        this.cipher = cipher;
        counterBlocks = new byte[text.length / cipher.getTextBlockSize()][cipher.getTextBlockSize()];
        byte[] delta = BytesUtil.splitInHalf(initialVector)[1];
        byte[] counter = cipher.encrypt(initialVector);

        for (int i = 0; i < text.length / cipher.getTextBlockSize(); i++) {
            counterBlocks[i] = counter;
            counter = getNextCounter(counter, delta);
        }
    }

    private byte[] getNextCounter(byte[] counter, byte[] delta) {
        byte[] result = new byte[counter.length];
        byte remind = 0;

        for (int i = 0; i < delta.length; i++) {
            result[i] = (byte) ((counter[counter.length - i - 1] + delta[delta.length - i - 1] + remind) % 256);
            remind = (byte) ((counter[counter.length - i - 1] + delta[delta.length - i - 1]) / 256);
        }

        if (remind != 0) {
            result[result.length - delta.length - 1] = (byte) ((counter[counter.length - delta.length - 1] + remind) % 256);
        }

        return result;
    }

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = BytesUtil.xor(cipher.decrypt(textBlock), counterBlocks[(indexBegin + i * textBlockSize) / textBlockSize]);
            System.arraycopy(cipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairIndexText(indexBegin, result);
    }
}
