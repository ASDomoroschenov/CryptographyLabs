package ru.mai.encryption.cipher_impl.mode.CBC;

import lombok.AllArgsConstructor;
import ru.mai.encryption.cipher_impl.mode.utils.utils_impl.PairMode;
import ru.mai.encryption.cipher_impl.mode.utils.utils_interface.IThreadTask;
import ru.mai.encryption.cipher_interface.ICipher;
import ru.mai.utils.BytesUtil;

@AllArgsConstructor
public class ThreadTaskDecryptCBC implements IThreadTask {
    private ICipher cipher;
    private byte[] initialVector;

    @Override
    public PairMode apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
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
            byte[] deCipherBlockText = BytesUtil.xor(deCipherBlock, cipher.decrypt(textBlock));
            System.arraycopy(deCipherBlockText, 0, result, i * textBlockSize, textBlockSize);
            deCipherBlock = textBlock.clone();
        }

        return new PairMode(indexBegin, result);
    }
}
