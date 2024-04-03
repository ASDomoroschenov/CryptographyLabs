package ru.mai.utils.utils_impl.thread_cipher.file.file_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.utils.utils_impl.thread_cipher.file.file_interface.IFileThreadCipher;
import ru.mai.utils.utils_impl.thread_cipher.file.file_interface.IFileThreadTask;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class FileThreadCipher implements IFileThreadCipher {
    private int sizeInputBlock;
    private int sizeOutputBlock;
    private IFileThreadTask threadTask;

    @Override
    public String cipher(String pathToInputFile, String pathToOutputFile, BigInteger firstPartKey, BigInteger secondPartKey) throws Exception {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);
        List<Future<byte[]>> futures = new ArrayList<>();

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            long skipValue = 0;
            long sizePartsThread = ((file.length() / sizeInputBlock) + availableProcessors - 1) / availableProcessors;
            long sizePartBytesThread = sizePartsThread * sizeInputBlock;

            while (skipValue < file.length()) {
                long finalSkipValue = skipValue;
                futures.add(service.submit(() -> threadTask.apply(pathToInputFile, finalSkipValue, sizePartBytesThread, firstPartKey, secondPartKey, sizeInputBlock, sizeOutputBlock)));
                skipValue += sizePartBytesThread;
            }
        } catch (IOException ex) {
            throw new Exception(ex);
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

        try (RandomAccessFile file = new RandomAccessFile(pathToOutputFile, "rw")) {
            for (Future<byte[]> future : futures) {
                byte[] text = future.get();
                file.write(text);
            }
        } catch (IOException | ExecutionException | InterruptedException ex) {
            throw new Exception(ex);
        }

        return pathToOutputFile;
    }
}
