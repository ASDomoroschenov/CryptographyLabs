package ru.mai.rijndael.padding.padding_impl;

import org.apache.commons.io.FileUtils;
import ru.mai.rijndael.padding.padding_interface.IPadding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ZerosPadding implements IPadding {
    @Override
    public byte[] addPAdding(byte[] bytes, int textBlockSize) throws IllegalArgumentException {
        if (bytes == null) {
            throw new IllegalArgumentException("Illegal bytes text");
        }
        if (textBlockSize <= 0) {
            throw new IllegalArgumentException("Illegal text block size");
        }

        byte[] bytesWithPadding = new byte[((bytes.length + textBlockSize - 1) / textBlockSize) * textBlockSize];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        return bytesWithPadding;
    }

    @Override
    public byte[] removePadding(byte[] bytes) throws IllegalArgumentException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        return bytes;
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
            paddingFile.write(padding);
        }

        return pathToAddPaddingFile;
    }

    @Override
    public String removePadding(String pathToFile) throws IllegalArgumentException, IOException {
        return pathToFile;
    }

    private String getOutputFileName(String pathToInputFile, String prefix) {
        int dotIndex = pathToInputFile.lastIndexOf('.');
        String baseName = pathToInputFile.substring(0, dotIndex);
        String extension = pathToInputFile.substring(dotIndex);
        return baseName + prefix + extension;
    }
}