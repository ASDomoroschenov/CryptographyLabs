package ru.mai.cipher.cipher_impl.DES.key_generate;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_interface.IRoundKeyGenerator;

@AllArgsConstructor
public class DESEncryptKeyGenerator implements IRoundKeyGenerator {
    private final DESKeyGenerator keyGenerator;

    @Override
    public byte[][] generate(byte[] key) throws IllegalArgumentException {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("Illegal bytes key");
        }

        return keyGenerator.getRoundKeys();
    }
}
