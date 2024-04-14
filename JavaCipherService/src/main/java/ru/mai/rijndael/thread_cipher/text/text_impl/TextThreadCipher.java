package ru.mai.rijndael.thread_cipher.text.text_impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.thread_cipher.collect.collect_interface.ICollectText;
import ru.mai.rijndael.thread_cipher.text.text_interface.ITextThreadCipher;
import ru.mai.rijndael.thread_cipher.text.text_interface.ITextThreadTask;
import ru.mai.rijndael.thread_cipher.text.utils.PairIndexText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class TextThreadCipher implements ITextThreadCipher {
    private int blockSize;
    private ITextThreadTask threadTask;
    private ICollectText collectText;

    @Override
    public byte[] cipher(byte[] text) throws IllegalArgumentException, InterruptedException, ExecutionException {
        if (text == null) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        List<Future<PairIndexText>> futures = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        int countBlocks = ((text.length / blockSize) + availableProcessors - 1) / availableProcessors;

        for (int i = 0; i < text.length; i += blockSize * countBlocks) {
            int finalI = i;
            int finalCountBlocks = i + blockSize * countBlocks < text.length ? countBlocks : (text.length - i) / blockSize;
            futures.add(service.submit(() -> threadTask.apply(text, finalI, blockSize, finalCountBlocks)));
        }

        byte[] result;

        try {
            result = collectText.collect(futures, text.length);
        } catch (InterruptedException ex) {
            throw new InterruptedException(ex.getMessage());
        } catch (ExecutionException ex) {
            throw new ExecutionException(ex);
        } finally {
            service.shutdown();

            try {
                if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        return result;
    }
}