package ru.mai.utils.utils_impl.thread_cipher.text.text_impl;

import lombok.AllArgsConstructor;
import ru.mai.RSA.RSA;
import ru.mai.utils.utils_impl.thread_cipher.text.PairIndexText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_interface.ITextThreadTask;

import java.math.BigInteger;

@AllArgsConstructor
public class TextThreadTaskCipher implements ITextThreadTask {
    private RSA rsa;

    @Override
    public PairIndexText apply(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey, int indexBegin, int countBlocks, int sizeInputBlock, int sizeOutputBlock) {
        byte[] result = new byte[countBlocks * sizeOutputBlock];

        for (int i = 0; i < sizeInputBlock * countBlocks; i += sizeInputBlock) {
            byte[] block = new byte[sizeInputBlock + 1];
            System.arraycopy(text, indexBegin + i, block, 1, sizeInputBlock);
            block = rsa.cipherConversionBlock(block, firstPartKey, secondPartKey, sizeOutputBlock);
            System.arraycopy(block, 0, result, (i / sizeInputBlock) * sizeOutputBlock, sizeOutputBlock);
        }

        return new PairIndexText(indexBegin, result);
    }
}
