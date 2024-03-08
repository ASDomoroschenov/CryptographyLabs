package ru.mai.encryption.encryption_interface;

public interface ICipher {
    byte[] encrypt(byte[] bytes);

    byte[] decrypt(byte[] bytes);

    int getTextBlockSize();
}
