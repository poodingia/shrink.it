package org.uetmydinh.keygeneration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.uetmydinh.keygeneration.entity.Key;

import java.util.Optional;

public interface KeyRepository extends MongoRepository<Key, String> {
    Optional<Key> findFirstByIsUsedFalse();
}
