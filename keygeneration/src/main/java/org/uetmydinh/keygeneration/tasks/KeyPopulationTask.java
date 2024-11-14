package org.uetmydinh.keygeneration.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.keygeneration.repository.KeyRepository;
import org.uetmydinh.keygeneration.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Component
@Profile("populate")
public class KeyPopulationTask implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(KeyPopulationTask.class);
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int KEY_LENGTH = 5;
    private static final long TOTAL_KEYS = (long) Math.pow(CHARACTERS.length, KEY_LENGTH);
    private static final int BATCH_SIZE = 1000000;
    private static final String FILE_PATH = "latestKeyIndex.txt";

    private final KeyRepository keyRepository;

    @Autowired
    public KeyPopulationTask(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Override
    public void run(String... args) {
        long startIdx = 0;
        try {
            startIdx = Long.parseLong(FileUtil.readFromFile(FILE_PATH));
        } catch (IOException | NumberFormatException e) {
            logger.warn("Could not read the latest key index from file, starting from 0.");
        }

        long existingKeyCount = keyRepository.count();
        if (existingKeyCount >= TOTAL_KEYS) {
            logger.info("All keys have already been generated.");
            return;
        } else if (existingKeyCount > 0) {
            logger.info("Resuming key generation from index {}...", startIdx);
        }

        final long startIndex = startIdx;

        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);

        try {
            customThreadPool.submit(() -> {
                AtomicLong processedKeys = new AtomicLong(startIndex);

                // Use a parallel stream to generate keys in parallel
                IntStream.rangeClosed((int) startIndex, (int) TOTAL_KEYS - 1).parallel().forEach(index -> {
                    List<Key> batch = new ArrayList<>();
                    for (long i = (long) index * CHARACTERS.length; i < (long) (index + 1) * CHARACTERS.length; i++) {
                        String keyString = generateKeyFromIndex(i);
                        batch.add(new Key(keyString, false));

                        if (batch.size() >= BATCH_SIZE) {
                            keyRepository.saveAll(batch);
                            batch.clear();
                        }

                        long processed = processedKeys.incrementAndGet();
                        if (processed % 10000 == 0) {
                            double progress = (double) processed / TOTAL_KEYS * 100.0;
                            logger.info("[{}%] Generated {} keys so far...", progress, processed);
                            try {
                                FileUtil.writeToFile(FILE_PATH, String.valueOf(processed));
                            } catch (IOException e) {
                                logger.error("Error writing the latest key index to file", e);
                            }
                        }
                    }

                    // Save any remaining keys in the batch
                    if (!batch.isEmpty()) {
                        keyRepository.saveAll(batch);
                    }
                });
            }).get();
        } catch (Exception e) {
            logger.error("Error during key generation", e);
        } finally {
            customThreadPool.shutdown();
        }

        logger.info("Key population completed.");
    }

    /**
     * Generates a key string based on the current index in a lexicographical order.
     *
     * @param index The index of the key in the lexicographical sequence.
     * @return The generated key string.
     */
    private String generateKeyFromIndex(long index) {
        StringBuilder keyBuilder = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            keyBuilder.insert(0, CHARACTERS[(int) (index % CHARACTERS.length)]);
            index /= CHARACTERS.length;
        }
        return keyBuilder.toString();
    }
}