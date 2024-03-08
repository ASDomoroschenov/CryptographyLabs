package ru.mai.encryption.cipher_interface;

public interface ICipher {
    byte[] encrypt(byte[] bytes);

    byte[] decrypt(byte[] bytes);

    int getTextBlockSize();
}
