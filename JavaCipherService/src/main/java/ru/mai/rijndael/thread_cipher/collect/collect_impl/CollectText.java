package ru.mai.rijndael.thread_cipher.collect.collect_impl;

import ru.mai.rijndael.thread_cipher.collect.collect_interface.ICollectText;
import ru.mai.rijndael.thread_cipher.text.utils.PairIndexText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CollectText implements ICollectText {
    @Override
    public byte[] collect(List<Future<PairIndexText>> futures, int textLength) throws ExecutionException, InterruptedException {
        byte[] result = new byte[textLength];

        while (!futures.isEmpty()) {
            List<Future<PairIndexText>> listNotDoneFuture = futures.stream().filter(item -> !item.isDone()).toList();

            for (Future<PairIndexText> future : futures) {
                if (!listNotDoneFuture.contains(future)) {
                    PairIndexText pair = future.get();
                    System.arraycopy(pair.getText(), 0, result, pair.getIndex(), pair.getText().length);
                }
            }

            futures = listNotDoneFuture;
        }

        return result;
    }
}