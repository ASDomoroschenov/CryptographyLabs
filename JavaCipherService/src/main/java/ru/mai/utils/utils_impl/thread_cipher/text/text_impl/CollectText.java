package ru.mai.utils.utils_impl.thread_cipher.text.text_impl;

import ru.mai.utils.utils_impl.thread_cipher.text.PairIndexText;
import ru.mai.utils.utils_impl.thread_cipher.text.text_interface.ICollectText;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CollectText implements ICollectText {
    @Override
    public byte[] collect(List<Future<PairIndexText>> futures, int textLength) throws ExecutionException, InterruptedException {
        byte[] result = new byte[textLength];
        int length = 0;

        for (Future<PairIndexText> future : futures) {
            PairIndexText pair = future.get();
            System.arraycopy(pair.getText(), 0, result, length, pair.getText().length);
            length += pair.getText().length;
        }

        return result;
    }
}
