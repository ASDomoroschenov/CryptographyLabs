package ru.mai.cipher.cipher_impl.padding;

import org.apache.commons.io.FileUtils;
import ru.mai.cipher.cipher_interface.IPadding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ANSIX923Padding implements IPadding {
    @Override
    public byte[] addPAdding(byte[] bytes, int textBlockSize) throws IllegalArgumentException {
        if (bytes == null) {
            throw new IllegalArgumentException("Illegal bytes text");
        }
        if (textBlockSize <= 0) {
            throw new IllegalArgumentException("Illegal text block size");
        }

        byte valuePadding = (byte) (textBlockSize - bytes.length % textBlockSize);
        byte[] bytesWithPadding = new byte[bytes.length + valuePadding];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        for (int i = 0; i < bytesWithPadding.length - bytes.length - 1; i++) {
            bytesWithPadding[bytes.length + i] = 0;
        }

        bytesWithPadding[bytesWithPadding.length - 1] = valuePadding;

        return bytesWithPadding;
    }

    @Override
    public byte[] removePadding(byte[] bytes) throws IllegalArgumentException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        byte valuePadding = bytes[bytes.length - 1];

        if (valuePadding > bytes.length) {
            throw new IllegalArgumentException("Illegal size padding");
        }

        byte[] bytesWithoutPadding = new byte[bytes.length - valuePadding];

        System.arraycopy(bytes, 0, bytesWithoutPadding, 0, bytes.length - valuePadding);

        return bytesWithoutPadding;
    }

    @Override
    public String addPAdding(String pathToFile, int textBlockSize) throws IllegalArgumentException, IOException {
        String pathToAddPaddingFile = getOutputFileName(pathToFile, "_add_padding");
        FileUtils.copyFile(new File(pathToFile), new File(pathToAddPaddingFile));

        try (RandomAccessFile inputFile = new RandomAccessFile(pathToFile, "r");
             RandomAccessFile paddingFile = new RandomAccessFile(pathToAddPaddingFile, "rw")) {
            paddingFile.seek(inputFile.length());
            byte valuePadding = (byte) (textBlockSize - inputFile.length() % textBlockSize);
            byte[] padding = new byte[valuePadding];
            padding[padding.length - 1] = valuePadding;
            paddingFile.write(padding);
        }

        return pathToAddPaddingFile;
    }

    @Override
    public String removePadding(String pathToFile) throws IllegalArgumentException, IOException {
        String pathToRemovePaddingFile = getOutputFileName(pathToFile, "_remove_padding");
        byte valuePadding = 0;
        byte[] buffer = null;

        try (RandomAccessFile inputFile = new RandomAccessFile(pathToFile, "r")) {
            inputFile.seek(inputFile.length() - 1);
            valuePadding = inputFile.readByte();
            buffer = new byte[(int) (inputFile.length() - valuePadding)];
        }

        try (RandomAccessFile inputFile = new RandomAccessFile(pathToFile, "r");
             RandomAccessFile paddingFile = new RandomAccessFile(pathToRemovePaddingFile, "rw")) {
            inputFile.read(buffer);
            paddingFile.write(buffer);
        }

        return pathToRemovePaddingFile;
    }

    private String getOutputFileName(String pathToInputFile, String prefix) {
        int dotIndex = pathToInputFile.lastIndexOf('.');
        String baseName = pathToInputFile.substring(0, dotIndex);
        String extension = pathToInputFile.substring(dotIndex);
        return baseName + prefix + extension;
    }
}
