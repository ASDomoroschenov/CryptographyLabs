package ru.mai.encryption.cipher_impl.mode.OFB;

import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.PairMode;
import ru.mai.encryption.cipher_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.encryption.cipher_interface.ICipher;
import ru.mai.utils.BytesUtil;

public class ThreadTaskOFB implements IThreadTask {
    private final byte[][] keyBlocks;

    public ThreadTaskOFB(ICipher cipher, byte[] text, byte[] initialVector) {

        keyBlocks = new byte[text.length / cipher.getTextBlockSize()][];
        byte[] keyBlock = initialVector;

        for (int i = 0; i < text.length / cipher.getTextBlockSize() ; i++) {
            keyBlocks[i] = keyBlock.clone();
            keyBlock = cipher.encrypt(keyBlock);
        }
    }

    @Override
    public PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] cipherBlockText = BytesUtil.xor(textBlock, keyBlocks[(indexBegin + i * textBlockSize) / textBlockSize]);
            System.arraycopy(cipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairMode(indexBegin, result);
    }
}
