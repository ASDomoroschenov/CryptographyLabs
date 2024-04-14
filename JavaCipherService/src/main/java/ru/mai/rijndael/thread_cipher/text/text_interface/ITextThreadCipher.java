package ru.mai.rijndael.thread_cipher.text.text_interface;

import java.util.concurrent.ExecutionException;

public interface ITextThreadCipher {
    byte[] cipher(byte[] text) throws ExecutionException, InterruptedException;
}
