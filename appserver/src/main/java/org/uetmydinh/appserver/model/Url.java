package org.uetmydinh.appserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@Document("urls")
public class Url {
    private String id;
    private String longUrl;
    private Instant createdAt;
    private Instant lastVisitedAt;
    private int visitCount;
}
