package ru.mai.RSA.padding;

public interface IPadding {
    byte[] addPAdding(byte[] bytes, int numBytes) throws IllegalArgumentException;

    byte[] removePadding(byte[] bytes) throws IllegalArgumentException;

    String addPAdding(String pathToFile, int numBytes) throws Exception;

    String removePadding(String pathToFile) throws Exception;
}