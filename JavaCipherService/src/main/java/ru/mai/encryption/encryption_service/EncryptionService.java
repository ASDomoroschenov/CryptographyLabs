package ru.mai.encryption.encryption_service;

import ru.mai.encryption.encryption_impl.DES.DES;
import ru.mai.encryption.encryption_impl.mode.*;
import ru.mai.encryption.encryption_impl.padding.ANSIX923Padding;
import ru.mai.encryption.encryption_impl.padding.ISO10126Padding;
import ru.mai.encryption.encryption_impl.padding.PKCS7Padding;
import ru.mai.encryption.encryption_impl.padding.ZerosPadding;
import ru.mai.encryption.encryption_interface.CipherMode;
import ru.mai.encryption.encryption_interface.Padding;
import ru.mai.encryption.encryption_interface.SymmetricCipher;

import java.util.Arrays;

public class EncryptionService {
    public enum EncryptionMode {
        ECB,
        CBC,
        PCBC,
        CFB,
        OFB,
        CTR,
        RANDOM_DELTA
    }

    public enum StuffingMode {
        ZEROS,
        ANSI_X_923,
        PKCS7,
        ISO_10126
    }

    public enum CipherAlgorithm {
        DES
    }

    private byte[] key;
    private SymmetricCipher symmetricCipher;
    private CipherMode cipherMode;
    private Padding padding;
    private byte[] initialVector;

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, Object... additionalArgs) {
        this.key = key;
        this.symmetricCipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);
        initialVector = null;
    }

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, byte[] initialVector, Object... additionalArgs) {
        this.key = key;
        this.symmetricCipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);
        this.initialVector = initialVector;
    }

    public SymmetricCipher getCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        switch (cipherAlgorithm) {
            case DES -> {
                return new DES(key);
            }
        }

        return null;
    }

    public CipherMode getEncryptionMode(EncryptionMode encryptionMode) {
        switch (encryptionMode) {
            case ECB -> {
                return new ECBMode();
            }
            case CBC -> {
                return new CBCMode();
            }
            case PCBC -> {
                return new PCBCMode();
            }
            case CFB -> {
                return new CFBMode();
            }
            case OFB -> {
                return new OFBMode();
            }
            case CTR -> {
                return new CTRMode();
            }
            case RANDOM_DELTA -> {
                return new RandomDeltaMode();
            }
        }

        return null;
    }

    public Padding getStuffingMode(StuffingMode stuffingMode) {
        switch (stuffingMode) {
            case ZEROS -> {
                return new ZerosPadding();
            }
            case ANSI_X_923 -> {
                return new ANSIX923Padding();
            }
            case PKCS7 -> {
                return new PKCS7Padding();
            }
            case ISO_10126 -> {
                return new ISO10126Padding();
            }
        }

        return null;
    }

    public byte[] encrypt(byte[] text) {
        int textBlockSize = symmetricCipher.getTextBlockSize();

        text = padding.addPAdding(text, textBlockSize);

        byte[] textBlock = new byte[textBlockSize];
        byte[] blockToCouple = initialVector;
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += textBlockSize) {
            System.arraycopy(text, i, textBlock, 0, textBlockSize);
            byte[] cipherTextBlock = symmetricCipher.encrypt(cipherMode.couple(textBlock, blockToCouple));
            blockToCouple = cipherTextBlock.clone();
            System.arraycopy(cipherTextBlock, 0, result, i, cipherTextBlock.length);
        }

        return result;
    }

    public String encrypt(String inputFilePath) {
        return null;
    }

    public byte[] decipher(byte[] text) {
        int textBlockSize = symmetricCipher.getTextBlockSize();
        byte[] textBlock = new byte[textBlockSize];
        byte[] blockToCouple = initialVector;
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += textBlockSize) {
            System.arraycopy(text, i, textBlock, 0, textBlockSize);
            byte[] cipherTextBlock = symmetricCipher.decrypt(cipherMode.couple(textBlock, blockToCouple));
            blockToCouple = cipherTextBlock.clone();
            System.arraycopy(cipherTextBlock, 0, result, i, cipherTextBlock.length);
        }

        return padding.removePadding(result);
    }

    public String decipher(String inputFilePath) {
        return null;
    }
}
