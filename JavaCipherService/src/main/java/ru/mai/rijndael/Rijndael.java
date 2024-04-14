package ru.mai.rijndael;

import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.padding.padding_interface.IPadding;
import ru.mai.rijndael.thread_cipher.collect.collect_impl.CollectText;
import ru.mai.rijndael.thread_cipher.file.file_impl.FileThreadCipher;
import ru.mai.rijndael.thread_cipher.file.file_impl.FileThreadTaskCipher;
import ru.mai.rijndael.thread_cipher.file.file_interface.IFileThreadCipher;
import ru.mai.rijndael.thread_cipher.text.text_impl.TextThreadCipher;
import ru.mai.rijndael.thread_cipher.text.text_impl.TextThreadTaskDecrypt;
import ru.mai.rijndael.thread_cipher.text.text_impl.TextThreadTaskEncrypt;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
public abstract class Rijndael {
    public enum PaddingMode {
        ZEROS,
        ANSI_X_923,
        PKCS7,
        ISO_10126
    }

    protected int countBitsBlock;
    protected byte[][] key;
    protected int countRounds;
    protected IPadding padding;

    public String encryptFile(String pathToInputFile) {
        String encryptFile = null;
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;

        try {
            String fileWithPadding = padding.addPAdding(pathToInputFile, sizeBlockBytes);
            encryptFile = new FileThreadCipher(
                    sizeBlockBytes,
                    new FileThreadTaskCipher(this)
            ).cipher(fileWithPadding, addPostfixToFileName(pathToInputFile, "_enc"), IFileThreadCipher.CipherAction.ENCRYPT);

            Files.delete(Path.of(fileWithPadding));
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return encryptFile;
    }

    public String decryptFile(String pathToInputFile) {
        String decryptFile = null;
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;

        try {
            decryptFile = new FileThreadCipher(
                    sizeBlockBytes,
                    new FileThreadTaskCipher(this)
            ).cipher(pathToInputFile, addPostfixToFileName(pathToInputFile, "_dec"), IFileThreadCipher.CipherAction.DECRYPT);
            String removePaddingFile = padding.removePadding(decryptFile);

            if (!(new File(removePaddingFile).renameTo(new File(decryptFile)))) {
                log.error("Error while renaming file");
            }
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return decryptFile;
    }

    public byte[] encrypt(byte[] text) {
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;

        try {
            return new TextThreadCipher(
                    sizeBlockBytes,
                    new TextThreadTaskEncrypt(this),
                    new CollectText()
            ).cipher(padding.addPAdding(text, sizeBlockBytes));
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return new byte[0];
    }

    public byte[] decrypt(byte[] text) {
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;

        try {
            return padding.removePadding(new TextThreadCipher(
                    sizeBlockBytes,
                    new TextThreadTaskDecrypt(this),
                    new CollectText()
            ).cipher(text));
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return new byte[0];
    }

    public byte[] encryptWithoutPadding(byte[] text) {
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;
        byte[] textBlock = new byte[sizeBlockBytes];
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += sizeBlockBytes) {
            System.arraycopy(text, i, textBlock, 0, sizeBlockBytes);
            byte[] encryptedBlock = encryptBlock(textBlock);
            System.arraycopy(encryptedBlock, 0, result, i, sizeBlockBytes);
        }

        return result;
    }

    public byte[] decryptWithoutPadding(byte[] text) {
        int sizeBlockBytes = countBitsBlock / Byte.SIZE;
        byte[] textBlock = new byte[sizeBlockBytes];
        byte[] result = new byte[text.length];

        for (int i = 0; i < text.length; i += sizeBlockBytes) {
            System.arraycopy(text, i, textBlock, 0, sizeBlockBytes);
            byte[] decryptedBlock = decryptBlock(textBlock);
            System.arraycopy(decryptedBlock, 0, result, i, sizeBlockBytes);
        }

        return result;
    }

    public byte[] encryptBlock(byte[] text) {
        byte[][] state = convertArrayToMatrix(text, 4, countBitsBlock / 32);

        state = beforeRounds(state);

        for (int i = 1; i < countRounds; i++) {
            state = round(state, i);
        }

        state = finalRound(state);

        return convertMatrixToArray(state);
    }

    public byte[] decryptBlock(byte[] text) {
        byte[][] state = convertArrayToMatrix(text, 4, countBitsBlock / 32);

        state = invFinalRound(state);

        for (int i = countRounds - 1; i >= 1; i--) {
            state = invRound(state, i);
        }

        state = afterRounds(state);

        return convertMatrixToArray(state);
    }

    public byte[][] round(byte[][] state, int numRound) {
        state = subBytes(state);
        state = shiftRows(state);
        state = mixColumns(state);
        state = addRoundKey(state, getRoundKey(numRound));
        return state;
    }

    public byte[][] invRound(byte[][] state, int numRound) {
        state = addRoundKey(state, getRoundKey(numRound));
        state = invMixColumns(state);
        state = invShiftRows(state);
        state = invSubBytes(state);
        return state;
    }

    public byte[][] finalRound(byte[][] state) {
        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, getRoundKey(countRounds));
        return state;
    }

    public byte[][] invFinalRound(byte[][] state) {
        state = addRoundKey(state, getRoundKey(countRounds));
        state = invShiftRows(state);
        state = invSubBytes(state);
        return state;
    }

    public byte[][] beforeRounds(byte[][] state) {
        return addRoundKey(state, getRoundKey(0));
    }

    public byte[][] afterRounds(byte[][] state) {
        return addRoundKey(state, getRoundKey(0));
    }

    public abstract byte[][] convertArrayToMatrix(byte[] array, int countRows, int countColumns);

    public abstract byte[] convertMatrixToArray(byte[][] matrix);

    public abstract byte[][] subBytes(byte[][] state);

    public abstract byte[][] shiftRows(byte[][] state);

    public abstract byte[][] mixColumns(byte[][] state);


    public abstract byte[][] invSubBytes(byte[][] state);

    public abstract byte[][] invShiftRows(byte[][] state);

    public abstract byte[][] invMixColumns(byte[][] state);


    public abstract byte[][] addRoundKey(byte[][] state, byte[][] roundKey);

    public abstract byte[][] getRoundKey(int numRound);

    private String addPostfixToFileName(String pathToInputFile, String postfix) {
        int dotIndex = pathToInputFile.lastIndexOf('.');
        String baseName = pathToInputFile.substring(0, dotIndex);
        String extension = pathToInputFile.substring(dotIndex);
        return baseName + postfix + extension;
    }
}
