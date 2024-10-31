package org.uetmydinh.appserver.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.uetmydinh.appserver.model.Url;

@Repository
public interface UrlRepository extends MongoRepository<Url, String> {
}
