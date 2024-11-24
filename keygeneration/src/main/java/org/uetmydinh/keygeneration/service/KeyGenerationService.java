package org.uetmydinh.keygeneration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.keygeneration.repository.KeyRepository;

import java.util.Optional;

@Service
public class KeyGenerationService {
    private final KeyRepository keyRepository;

    @Autowired
    public KeyGenerationService(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public Optional<Key> getAvailableKey() {
        return keyRepository.findFirstByIsUsedFalse();
    }

    public void markKeyAsUsed(Key key) {
        key.setIsUsed(true);
        keyRepository.save(key);
    }
}
