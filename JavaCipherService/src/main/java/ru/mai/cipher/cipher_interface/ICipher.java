package ru.mai.cipher.cipher_interface;

public interface ICipher {
    byte[] encrypt(byte[] bytes) throws IllegalArgumentException;

    byte[] decrypt(byte[] bytes) throws IllegalArgumentException;

    int getTextBlockSize();

    int getKeySize();
}
