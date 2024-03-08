package ru.mai.encryption.cipher_interface;

public interface IRoundKeyGenerator {
    byte[][] generate(byte[] key);
}
