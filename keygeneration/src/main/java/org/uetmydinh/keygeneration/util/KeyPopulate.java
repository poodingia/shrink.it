package org.uetmydinh.keygeneration.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.keygeneration.repository.KeyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

@Component
@Profile("populate")
public class KeyPopulate implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(KeyPopulate.class);
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int KEY_LENGTH = 5;
    private static final long TOTAL_KEYS = (long) Math.pow(CHARACTERS.length, KEY_LENGTH);
    private static final int BATCH_SIZE = 1000000;

    private final KeyRepository keyRepository;

    @Autowired
    public KeyPopulate(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Override
    public void run(String... args) {
        long existingKeyCount = keyRepository.count();
        if (existingKeyCount >= TOTAL_KEYS) {
            logger.info("All keys have already been generated.");
            return;
        } else if (existingKeyCount > 0) {
            keyRepository.deleteAll();
        }

        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

        try {
            customThreadPool.submit(() -> {
                AtomicLong processedKeys = new AtomicLong(0);

                // Use a parallel stream to generate keys in parallel
                IntStream.rangeClosed(0, (int) TOTAL_KEYS - 1).parallel().forEach(index -> {
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
                            logger.info("[{}%] Generated {} keys so far...", processed/TOTAL_KEYS * 100.000,  processed);
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