package ru.mai.cipher.cipher_impl.mode.utils.utils_interface;

import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.PairIndexText;

import java.util.concurrent.ExecutionException;

public interface IThreadTask {
    PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) throws ExecutionException, InterruptedException;
}
