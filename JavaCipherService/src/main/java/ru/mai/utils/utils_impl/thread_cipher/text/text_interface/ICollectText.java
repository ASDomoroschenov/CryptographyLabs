package ru.mai.utils.utils_impl.thread_cipher.text.text_interface;

import ru.mai.utils.utils_impl.thread_cipher.text.PairIndexText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ICollectText {
    byte[] collect(List<Future<PairIndexText>> futures, int textLength) throws ExecutionException, InterruptedException;
}