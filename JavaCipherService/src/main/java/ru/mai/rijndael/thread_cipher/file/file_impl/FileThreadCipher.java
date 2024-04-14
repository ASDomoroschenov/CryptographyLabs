package ru.mai.rijndael.thread_cipher.file.file_impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mai.rijndael.thread_cipher.file.file_interface.IFileThreadCipher;
import ru.mai.rijndael.thread_cipher.file.file_interface.IFileThreadTaskCipher;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@AllArgsConstructor
public class FileThreadCipher implements IFileThreadCipher {
    private int sizeBlockBytes;
    private IFileThreadTaskCipher fileThreadTaskCipher;

    @Override
    public String cipher(String pathToInputFile, String pathToOutputFile, CipherAction action) throws IOException, ExecutionException, InterruptedException {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        List<Future<byte[]>> futures = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);

        try (RandomAccessFile file = new RandomAccessFile(pathToInputFile, "r")) {
            long skipValue = 0;
            long sizePartsThread = ((file.length() / sizeBlockBytes) + availableProcessors - 1) / availableProcessors;
            long sizePartBytesThread = sizePartsThread * sizeBlockBytes;

            while (skipValue < file.length()) {
                long finalSkipValue = skipValue;
                futures.add(service.submit(() -> fileThreadTaskCipher.apply(pathToInputFile, finalSkipValue, sizePartBytesThread, action)));
                skipValue += sizePartBytesThread;
            }

        } catch (IOException ex) {
            throw new IOException(ex);
        }

        try (RandomAccessFile file = new RandomAccessFile(pathToOutputFile, "rw")) {
            for (Future<byte[]> future : futures) {
                byte[] text = future.get();
                file.write(text);
            }
        } catch (IOException ex) {
            throw new IOException(ex);
        } catch (InterruptedException ex) {
            throw new InterruptedException(ex.getMessage());
        } catch (ExecutionException ex) {
            throw new ExecutionException(ex);
        }

        service.shutdown();

        try {
            if (!service.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return pathToOutputFile;
    }
}
