package ru.mai.rijndael.thread_cipher.text.text_impl;

import lombok.AllArgsConstructor;
import ru.mai.rijndael.Rijndael;
import ru.mai.rijndael.thread_cipher.text.text_interface.ITextThreadTask;
import ru.mai.rijndael.thread_cipher.text.utils.PairIndexText;

@AllArgsConstructor
public class TextThreadTaskDecrypt implements ITextThreadTask {
    private Rijndael rijndael;

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) {
        byte[] blocks = new byte[countBlocks * textBlockSize];
        System.arraycopy(text, indexBegin, blocks, 0, countBlocks * textBlockSize);
        return new PairIndexText(indexBegin, rijndael.decryptWithoutPadding(blocks));
    }
}
