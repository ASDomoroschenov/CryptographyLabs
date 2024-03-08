package ru.mai.encryption.cipher_impl.key_generate;

import lombok.AllArgsConstructor;
import ru.mai.encryption.cipher_interface.IRoundKeyGenerator;

@AllArgsConstructor
public class DESEncryptKeyGenerator implements IRoundKeyGenerator {
    private final DESKeyGenerator keyGenerator;

    @Override
    public byte[][] generate(byte[] key) {
        return keyGenerator.getRoundKeys();
    }
}
