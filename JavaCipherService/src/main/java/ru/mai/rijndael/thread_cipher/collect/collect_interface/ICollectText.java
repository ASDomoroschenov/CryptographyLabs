package ru.mai.rijndael.thread_cipher.collect.collect_interface;

import ru.mai.rijndael.thread_cipher.text.utils.PairIndexText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ICollectText {
    byte[] collect(List<Future<PairIndexText>> futures, int textLength) throws ExecutionException, InterruptedException;
}
