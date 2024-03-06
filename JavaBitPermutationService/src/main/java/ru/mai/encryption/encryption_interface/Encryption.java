package ru.mai.encryption.encryption_interface;

public interface Encryption {
    byte[] apply(byte[] bytes, byte[] roundKey);
}
