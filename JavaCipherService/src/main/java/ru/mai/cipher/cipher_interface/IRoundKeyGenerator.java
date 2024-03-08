package ru.mai.cipher.cipher_interface;

public interface IRoundKeyGenerator {
    byte[][] generate(byte[] key);
}
