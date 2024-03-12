package ru.mai.cipher.cipher_impl.mode.CFB;

import lombok.AllArgsConstructor;
import ru.mai.utils.utils_impl.PairIndexText;
import ru.mai.utils.utils_interface.IThreadTask;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.utils.utils_impl.BytesUtil;

@AllArgsConstructor
public class ThreadTaskDecryptCFB implements IThreadTask {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] deCipherBlock;
        byte[] result = new byte[countBlocks * textBlockSize];
        byte[] textBlock = new byte[textBlockSize];

        if (indexBegin == 0) {
            deCipherBlock = initialVector;
        } else {
            deCipherBlock = new byte[textBlockSize];
            System.arraycopy(text, indexBegin - textBlockSize, deCipherBlock, 0, textBlockSize);
        }

        for (int i = 0; i < countBlocks; i++) {
            System.arraycopy(text, indexBegin + i * textBlockSize, textBlock, 0, textBlockSize);
            byte[] deCipherBlockText = BytesUtil.xor(cipher.encrypt(deCipherBlock), textBlock);
            System.arraycopy(deCipherBlockText, 0, result, i * textBlockSize, textBlockSize);
            deCipherBlock = textBlock.clone();
        }

        return new PairIndexText(indexBegin, result);
    }
}
