package ru.mai.cipher.cipher_impl.mode.utils.utils_impl;

import lombok.AllArgsConstructor;
import ru.mai.cipher.cipher_impl.mode.utils.utils_interface.ICollectText;
import ru.mai.cipher.cipher_impl.mode.utils.utils_interface.IThreadCipher;
import ru.mai.cipher.cipher_impl.mode.utils.utils_interface.IThreadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@AllArgsConstructor
public class ThreadCipher implements IThreadCipher {
    private int blockSize;
    private IThreadTask threadTask;
    private ICollectText collectText;

    @Override
    public byte[] cipher(byte[] text) throws ExecutionException, InterruptedException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<PairMode>> futures = new ArrayList<>();
        int countBlocks = ((text.length / blockSize) + availableProcessors - 1) / availableProcessors;

        for (int i = 0; i < text.length; i += blockSize * countBlocks) {
            int finalI = i;
            int finalCountBlocks = i + blockSize * countBlocks < text.length ? countBlocks : (text.length - i) / blockSize;
            futures.add(service.submit(() -> threadTask.apply(text, finalI, blockSize, finalCountBlocks)));
        }

        byte[] result = collectText.collect(futures, text.length);

        service.shutdown();

        try {
            if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
        }

        return result;
    }
}
