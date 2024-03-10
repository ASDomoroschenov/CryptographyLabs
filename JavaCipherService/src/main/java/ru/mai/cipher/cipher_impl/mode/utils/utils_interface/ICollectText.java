package ru.mai.cipher.cipher_impl.mode.utils.utils_interface;

import ru.mai.cipher.cipher_impl.mode.utils.utils_impl.PairIndexText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ICollectText {
    byte[] collect(List<Future<PairIndexText>> futures, int textLength) throws ExecutionException, InterruptedException;
}
