package ru.mai.encryption.encryption_interface;

import java.util.concurrent.ExecutionException;

public interface ICipherMode {
    byte[] encryptText(byte[] text) throws ExecutionException, InterruptedException;

    byte[] decryptText(byte[] text) throws ExecutionException, InterruptedException;
}
