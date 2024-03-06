package ru.mai.encryption.encryption_interface;

public interface Encryption {
    byte[] encrypt(byte[] bytes, byte[] roundKey);
}
