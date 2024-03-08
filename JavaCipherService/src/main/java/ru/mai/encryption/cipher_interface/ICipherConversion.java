package ru.mai.encryption.cipher_interface;

public interface ICipherConversion {
    byte[] apply(byte[] bytes, byte[] roundKey);
}
