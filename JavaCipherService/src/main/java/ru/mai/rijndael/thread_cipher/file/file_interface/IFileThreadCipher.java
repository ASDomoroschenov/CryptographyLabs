package ru.mai.rijndael.thread_cipher.file.file_interface;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IFileThreadCipher {
    enum CipherAction {
        ENCRYPT,
        DECRYPT
    }

    String cipher(String pathToInputFile, String pathToOutputFile, CipherAction action) throws IOException, ExecutionException, InterruptedException;
}
