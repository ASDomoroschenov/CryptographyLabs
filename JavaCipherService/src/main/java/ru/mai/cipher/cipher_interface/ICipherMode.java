package ru.mai.cipher.cipher_interface;

import java.util.concurrent.ExecutionException;

public interface ICipherMode {
    byte[] encrypt(byte[] text) throws ExecutionException, InterruptedException;

    byte[] decrypt(byte[] text) throws ExecutionException, InterruptedException;
}
