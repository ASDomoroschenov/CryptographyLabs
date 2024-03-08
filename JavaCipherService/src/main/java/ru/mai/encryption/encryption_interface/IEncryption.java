package ru.mai.encryption.encryption_interface;

public interface IEncryption {
    byte[] apply(byte[] bytes, byte[] roundKey);
}
