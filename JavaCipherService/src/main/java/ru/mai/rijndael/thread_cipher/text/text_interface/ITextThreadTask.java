package ru.mai.rijndael.thread_cipher.text.text_interface;

import ru.mai.rijndael.thread_cipher.text.utils.PairIndexText;

import java.util.concurrent.ExecutionException;

public interface ITextThreadTask {
    PairIndexText apply(byte[] text, int indexBegin, int textBlockSize, int countBlocks) throws ExecutionException, InterruptedException;
}
