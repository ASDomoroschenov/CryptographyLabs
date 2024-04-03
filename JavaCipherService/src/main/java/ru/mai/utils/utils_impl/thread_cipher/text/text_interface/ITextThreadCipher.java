package ru.mai.utils.utils_impl.thread_cipher.text.text_interface;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface ITextThreadCipher {
    byte[] cipher(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey) throws ExecutionException, InterruptedException;
}
