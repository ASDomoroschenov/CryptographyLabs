package ru.mai.cipher.cipher_impl.DEAL.key_generate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DEALKeyGenerate {
    private static final int KEY_SIZE_128 = 16;
    private static final int KEY_SIZE_192 = 24;
    private static final int KEY_SIZE_256 = 32;
    private byte[] key;

    public byte[][] getRoundKeys() throws IllegalArgumentException {
        switch (key.length) {
            case KEY_SIZE_128 -> {
                return new DEAL128KeyGenerate(key).generate();
            }
            case KEY_SIZE_192 -> {
                return new DEAL192KeyGenerate(key).generate();
            }
            case KEY_SIZE_256 -> {
                return new DEAL256KeyGenerate(key).generate();
            }
        }

        return null;
    }

    public int getNumRounds() {
        switch (key.length) {
            case KEY_SIZE_128, KEY_SIZE_192 -> {
                return 6;
            }
            case KEY_SIZE_256 -> {
                return 8;
            }
        }

        return 0;
    }
}
