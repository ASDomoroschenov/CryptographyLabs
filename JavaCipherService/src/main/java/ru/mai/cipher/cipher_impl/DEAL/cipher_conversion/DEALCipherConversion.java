package ru.mai.cipher.cipher_impl.DEAL.cipher_conversion;

import ru.mai.cipher.cipher_impl.DES.DES;
import ru.mai.cipher.cipher_interface.ICipherConversion;

public class DEALCipherConversion implements ICipherConversion {
    @Override
    public byte[] apply(byte[] bytes, byte[] roundKey) throws IllegalArgumentException {
        return new DES(roundKey).encrypt(bytes);
    }
}
