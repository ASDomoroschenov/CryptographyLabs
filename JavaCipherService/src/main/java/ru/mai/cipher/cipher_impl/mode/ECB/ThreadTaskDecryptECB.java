package ru.mai.cipher.cipher_impl.mode.ECB;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.PairIndexText;
import ru.mai.cipher.cipher_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.cipher.cipher_interface.ICipher;

@AllArgsConstructor
public class ThreadTaskDecryptECB implements IThreadTask {
    private ICipher cipher;

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] deCipherBlockText = cipher.decrypt(textBlock);
            System.arraycopy(deCipherBlockText, 0, result, i * textBlockSize, textBlockSize);
        }

        return new PairIndexText(indexBegin, result);
    }
}
