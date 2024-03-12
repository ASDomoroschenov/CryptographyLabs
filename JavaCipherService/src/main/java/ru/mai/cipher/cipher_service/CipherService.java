package ru.mai.cipher.cipher_service;

import lombok.extern.slf4j.Slf4j;
import ru.mai.cipher.cipher_impl.DEAL.DEAL;
import ru.mai.cipher.cipher_impl.DES.DES;
import ru.mai.cipher.cipher_impl.mode.CBC.CBCMode;
import ru.mai.cipher.cipher_impl.mode.CFB.CFBMode;
import ru.mai.cipher.cipher_impl.mode.CTR.CTRMode;
import ru.mai.cipher.cipher_impl.mode.ECB.ECBMode;
import ru.mai.cipher.cipher_impl.mode.OFB.OFBMode;
import ru.mai.cipher.cipher_impl.mode.PCBC.PCBCMode;
import ru.mai.cipher.cipher_impl.mode.RD.RDMode;
import ru.mai.utils.utils_impl.CollectText;
import ru.mai.utils.utils_impl.ThreadCipher;
import ru.mai.cipher.cipher_impl.padding.ANSIX923Padding;
import ru.mai.cipher.cipher_impl.padding.ISO10126Padding;
import ru.mai.cipher.cipher_impl.padding.PKCS7Padding;
import ru.mai.cipher.cipher_impl.padding.ZerosPadding;
import ru.mai.cipher.cipher_interface.ICipher;
import ru.mai.cipher.cipher_interface.ICipherMode;
import ru.mai.cipher.cipher_interface.IPadding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class CipherService {
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
        DES,
        DEAL
    }

    public enum CipherActions {
        ENCRYPT,
        DECRYPT
    }

    private final byte[] key;
    private final ICipher cipher;
    private final ICipherMode cipherMode;
    private final IPadding padding;
    private final byte[] initialVector;

    public CipherService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, Object... additionalArgs) {
        this.key = key;
        this.cipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);
        initialVector = null;

        if (!this.cipher.checkKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }
    }

    public CipherService(byte[] key, CipherAlgorithm cipherAlgorithm, EncryptionMode encryptionMode, StuffingMode stuffingMode, byte[] initialVector, Object... additionalArgs) {
        this.key = key;
        this.initialVector = initialVector.clone();
        this.cipher = getCipherAlgorithm(cipherAlgorithm);
        this.cipherMode = getEncryptionMode(encryptionMode);
        this.padding = getStuffingMode(stuffingMode);

        if (initialVector.length != cipher.getTextBlockSize()) {
            throw new IllegalArgumentException("Invalid size initial vector");
        }
        if (!this.cipher.checkKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }
    }

    public ICipher getCipherAlgorithm(CipherAlgorithm cipherAlgorithm) {
        switch (cipherAlgorithm) {
            case DES -> {
                return new DES(key);
            }
            case DEAL -> {
                return new DEAL(key);
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
                return new RDMode(cipher, initialVector);
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

    public byte[] encrypt(byte[] text) {
        byte[] result = null;

        try {
            result = new ThreadCipher(
                    cipher.getTextBlockSize(),
                    new ThreadTaskEncryptText(cipherMode),
                    new CollectText()
            ).cipher(padding.addPAdding(text, cipher.getTextBlockSize()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return result;
    }

    public String encrypt(String pathToInputFile) throws IOException {
        String encryptFile = null;

        try {
            String fileWithPadding = padding.addPAdding(pathToInputFile, cipher.getTextBlockSize());
            encryptFile = cipherFile(fileWithPadding, getOutputFileName(pathToInputFile, "_enc"), CipherActions.ENCRYPT);
            new File(fileWithPadding).delete();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return encryptFile;
    }

    public byte[] decrypt(byte[] text) {
        byte[] result = null;

        try {
            result = padding.removePadding(new ThreadCipher(
                    cipher.getTextBlockSize(),
                    new ThreadTaskDecryptText(cipherMode),
                    new CollectText()
            ).cipher(text));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return result;
    }

    public String decrypt(String pathToInputFile) throws IOException {
        String decryptFile = null;

        try {
            decryptFile = cipherFile(pathToInputFile, getOutputFileName(pathToInputFile, "_dec"), CipherActions.DECRYPT);
            String removePaddingFile = padding.removePadding(decryptFile);
            new File(removePaddingFile).renameTo(new File(decryptFile));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return decryptFile;
    }

    private String cipherFile(String pathToInputFile, String pathToOutputFile, CipherActions cipherActions) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<byte[]>> futures = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            long skipValue = 0;
            long sizePartsThread = ((file.length() / cipher.getTextBlockSize()) + availableProcessors - 1) / availableProcessors;
            long sizePartBytesThread = sizePartsThread * cipher.getTextBlockSize();

            while (skipValue < file.length()) {
                long finalSkipValue = skipValue;
                futures.add(service.submit(() -> threadTaskCipherFile(pathToInputFile, finalSkipValue, sizePartBytesThread, cipherActions)));
                skipValue += sizePartBytesThread;
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        try (RandomAccessFile file = new RandomAccessFile(pathToOutputFile, "rw")) {
            for (Future<byte[]> future : futures) {
                byte[] text = future.get();
                file.write(text);
            }
        } catch (IOException | ExecutionException | InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        service.shutdown();

        try {
            if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        return pathToOutputFile;
    }

    private byte[] threadTaskCipherFile(String pathToInputFile, long skipValue, long sizePartBytesThread, CipherActions cipherActions) {
        byte[] text = new byte[(int) sizePartBytesThread];

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            file.seek(skipValue);
            int countBytes = file.read(text);

            if (countBytes != sizePartBytesThread) {
                byte[] trimText = new byte[countBytes];
                System.arraycopy(text, 0, trimText, 0, countBytes);
                text = trimText;
            }
        } catch (IOException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        switch (cipherActions) {
            case ENCRYPT -> {
                return cipherMode.encrypt(text);
            }
            case DECRYPT -> {
                return cipherMode.decrypt(text);
            }
        }

        return null;
    }

    private String getOutputFileName(String pathToInputFile, String prefix) {
        int dotIndex = pathToInputFile.lastIndexOf('.');
        String baseName = pathToInputFile.substring(0, dotIndex);
        String extension = pathToInputFile.substring(dotIndex);
        return baseName + prefix + extension;
    }
}
