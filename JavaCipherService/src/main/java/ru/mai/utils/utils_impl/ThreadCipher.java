package ru.mai.utils.utils_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_interface.ICollectText;
import ru.mai.utils.utils_interface.IThreadCipher;
import ru.mai.utils.utils_interface.IThreadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class ThreadCipher implements IThreadCipher {
    private int blockSize;
    private IThreadTask threadTask;
    private ICollectText collectText;

    @Override
    public byte[] cipher(byte[] text) throws IllegalArgumentException {
        if (text == null) {
            throw new IllegalArgumentException("Illegal bytes text");
        }

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<PairIndexText>> futures = new ArrayList<>();
        int countBlocks = ((text.length / blockSize) + availableProcessors - 1) / availableProcessors;

        for (int i = 0; i < text.length; i += blockSize * countBlocks) {
            int finalI = i;
            int finalCountBlocks = i + blockSize * countBlocks < text.length ? countBlocks : (text.length - i) / blockSize;
            futures.add(service.submit(() -> threadTask.apply(text, finalI, blockSize, finalCountBlocks)));
        }

        byte[] result = null;

        try {
            result = collectText.collect(futures, text.length);
        } catch (InterruptedException | ExecutionException ex) {
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        } finally {
            service.shutdown();

            try {
                if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
        }

        return result;
    }
}
