package ru.mai.encryption.encryption_impl.mode.ECB;

import lombok.AllArgsConstructor;
import ru.mai.encryption.encryption_impl.mode.utils.utils_impl.PairMode;
import ru.mai.encryption.encryption_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.encryption.encryption_interface.ISymmetricCipher;

@AllArgsConstructor
public class ThreadTaskDecryptECB implements IThreadTask {
    private ISymmetricCipher cipher;

    @Override
    public PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] deCipherBlockText = cipher.decrypt(textBlock);
            System.arraycopy(deCipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairMode(indexBegin, result);
    }
}
