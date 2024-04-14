package ru.mai.rijndael.thread_cipher.file.file_interface;

import java.io.IOException;

public interface IFileThreadTaskCipher {
    byte[] apply(String pathToInputFile, long skipValue, long sizePartBytesThread, IFileThreadCipher.CipherAction action) throws IOException;
}
