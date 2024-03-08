package ru.mai.encryption.encryption_service;

import ru.mai.encryption.encryption_impl.DES.DES;
import ru.mai.encryption.encryption_impl.mode.CBC.CBCMode;
import ru.mai.encryption.encryption_impl.mode.CFB.CFBMode;
import ru.mai.encryption.encryption_impl.mode.CTR.CTRMode;
import ru.mai.encryption.encryption_impl.mode.ECB.ECBMode;
import ru.mai.encryption.encryption_impl.mode.OFB.OFBMode;
import ru.mai.encryption.encryption_impl.mode.PCBC.PCBCMode;
import ru.mai.encryption.encryption_impl.mode.RD.RDMode;
import ru.mai.encryption.encryption_impl.padding.ANSIX923Padding;
import ru.mai.encryption.encryption_impl.padding.ISO10126Padding;
import ru.mai.encryption.encryption_impl.padding.PKCS7Padding;
import ru.mai.encryption.encryption_impl.padding.ZerosPadding;
import ru.mai.encryption.encryption_interface.ICipherMode;
import ru.mai.encryption.encryption_interface.IPadding;
import ru.mai.encryption.encryption_interface.ICipher;

import java.util.concurrent.ExecutionException;

public class EncryptionService {
    public enum EncryptionMode {
        ECB,
        CBC,
        PCBC,
        CFB,
        OFB,
        CTR,
        RD
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
    private ICipher cipher;
    private ICipherMode cipherMode;
    private IPadding padding;
    private byte[] initialVector;

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, Object... additionalArgs) {
        this.key = key;
        this.cipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);
        initialVector = null;
    }

    public EncryptionService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, byte[] initialVector, Object... additionalArgs) {
        this.key = key;
        this.initialVector = initialVector;
        this.cipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);
    }

    public ICipher getCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        switch (cipherAlgorithm) {
            case DES -> {
                return new DES(key);
            }
        }

        return null;
    }

    public ICipherMode getEncryptionMode(EncryptionMode encryptionMode) {
        switch (encryptionMode) {
            case ECB -> {
                return new ECBMode(cipher);
            }
            case CBC -> {
                return new CBCMode(cipher, initialVector);
            }
            case PCBC -> {
                return new PCBCMode(cipher, initialVector);
            }
            case CFB -> {
                return new CFBMode(cipher, initialVector);
            }
            case OFB -> {
                return new OFBMode(cipher, initialVector);
            }
            case CTR -> {
                return new CTRMode(cipher, initialVector);
            }
            case RD -> {
                return new RDMode(cipher);
            }
        }

        return null;
    }

    public IPadding getStuffingMode(StuffingMode stuffingMode) {
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

    public byte[] encrypt(byte[] text) throws ExecutionException, InterruptedException {
        return cipherMode.encrypt(padding.addPAdding(text, cipher.getTextBlockSize()));
    }

    public String encrypt(String inputFilePath) {
        return null;
    }

    public byte[] decipher(byte[] text) {
        return null;
    }

    public String decipher(String inputFilePath) {
        return null;
    }
}
