package ru.mai.encryption.encryption_interface;

public interface IRoundKeyGenerator {
    byte[][] generate(byte[] key);
}
