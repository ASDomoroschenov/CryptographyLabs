package ru.mai.utils.utils_impl.thread_cipher.text.text_interface;

import java.math.BigInteger;

public interface ITextThreadCipher {
    byte[] cipher(byte[] text, BigInteger firstPartKey, BigInteger secondPartKey) throws Exception;
}
