package ru.mai.RSA.stream_mode;

public interface IStreamCipher {
    byte[] encrypt(byte[] text);
    byte[] decrypt(byte[] text);
}
