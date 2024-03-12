package ru.mai.cipher.cipher_service;

import lombok.AllArgsConstructor;
import ru.mai.utils.utils_impl.PairIndexText;
import ru.mai.utils.utils_interface.IThreadTask;
import ru.mai.cipher.cipher_interface.ICipherMode;

import java.util.concurrent.ExecutionException;

@AllArgsConstructor
class ThreadTaskDecryptText implements IThreadTask {
    private ICipherMode cipherMode;

    @Override
    public PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) throws ExecutionException, InterruptedException {
        byte[] blocks = new byte[countBlocks * textBlockSize];
        System.arraycopy(text, indexBegin, blocks, 0, countBlocks * textBlockSize);
        return new PairIndexText(indexBegin, cipherMode.decrypt(blocks));
    }
}
