package ru.mai.RSA.padding;

import java.io.IOException;

public interface IPadding {
    byte[] addPAdding(byte[] bytes, int numBytes) throws IllegalArgumentException;

    byte[] removePadding(byte[] bytes) throws IllegalArgumentException;

    String addPAdding(String pathToFile, int numBytes) throws IllegalArgumentException, IOException;

    String removePadding(String pathToFile) throws IllegalArgumentException, IOException;
}