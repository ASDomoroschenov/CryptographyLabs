package ru.mai.encryption.cipher_impl.mode.utils.utils_interface;

import java.util.concurrent.ExecutionException;

public interface IThreadCipher {
    byte[] cipher(byte[] text) throws ExecutionException, InterruptedException;
}
