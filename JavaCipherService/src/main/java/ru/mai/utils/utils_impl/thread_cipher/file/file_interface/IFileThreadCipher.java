package ru.mai.utils.utils_impl.thread_cipher.file.file_interface;

import java.math.BigInteger;

public interface IFileThreadCipher {
    String cipher(String pathToInputFile, String pathToOutputFile, BigInteger firstPartKey, BigInteger secondPartKey) throws Exception;
}
