package ru.mai.utils.utils_impl.thread_cipher.file.file_interface;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public interface IFileThreadCipher {
    String cipher(String pathToInputFile, BigInteger firstPartKey, BigInteger secondPartKey) throws ExecutionException, InterruptedException;
}
