package ru.mai.utils.utils_impl.thread_cipher.file.file_interface;

import java.math.BigInteger;

public interface IFileThreadTask {
    byte[] apply(String pathToInputFile, long skipValue, long sizePartBytesThread, BigInteger firstPartKey, BigInteger secondPartKey, int sizeInputBlock, int sizeOutputBlock) throws Exception;
}
