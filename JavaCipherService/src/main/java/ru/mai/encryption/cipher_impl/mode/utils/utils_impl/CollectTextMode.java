package ru.mai.encryption.cipher_impl.mode.utils.utils_impl;

import ru.mai.encryption.cipher_impl.mode.utils.utils_interface.ICollectText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CollectTextMode implements ICollectText {
    @Override
    public byte[] collect(List<Future<PairMode>> futures, int textLength) throws ExecutionException, InterruptedException {
        byte[] result = new byte[textLength];

        while (!futures.isEmpty()) {
            List<Future<PairMode>> listNotDoneFuture = futures.stream().filter(item -> !item.isDone()).toList();

            for (Future<PairMode> future : futures) {
                if (!listNotDoneFuture.contains(future)) {
                    PairMode pair = future.get();
                    System.arraycopy(pair.getText(), 0, result, pair.getIndex(), pair.getText().length);
                }
            }

            futures = listNotDoneFuture;
        }

        return result;
    }
}
