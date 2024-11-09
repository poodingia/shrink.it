package org.uetmydinh.keygeneration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.uetmydinh.keygeneration.entity.Key;
import org.uetmydinh.keygeneration.repository.KeyRepository;

import java.util.Optional;

@Service
public class KeyGenerationService {
    private final KeyRepository keyRepository;
    private final MongoTemplate mongoTemplate;


    @Autowired
    public KeyGenerationService(KeyRepository keyRepository, MongoTemplate mongoTemplate) {
        this.keyRepository = keyRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<Key> getAvailableKey() {
        Query query = new Query(Criteria.where("isUsed").is(false));
        Update update = new Update().set("isUsed", true);
        Key key = mongoTemplate.findAndModify(query, update, Key.class);
        return Optional.ofNullable(key);
    }

    @Deprecated
    public void markKeyAsUsed(Key key) {
        key.setIsUsed(true);
        keyRepository.save(key);
    }
}
