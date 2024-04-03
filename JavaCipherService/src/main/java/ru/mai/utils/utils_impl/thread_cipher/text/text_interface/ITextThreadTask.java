package ru.mai.utils.utils_impl.thread_cipher.text.text_interface;

import ru.mai.utils.utils_impl.thread_cipher.text.PairIndexText;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface ITextThreadTask {
    PairIndexText apply(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey, int indexBegin, int countBlocks) throws ExecutionException, InterruptedException;
}