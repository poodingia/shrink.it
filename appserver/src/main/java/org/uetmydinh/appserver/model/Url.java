package org.uetmydinh.appserver.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document("urls")
public class Url {
    private String id;
    private String longUrl;
    private Instant createdAt;
    private Instant lastVisitedAt;
    private int visitCount;

    @JsonCreator
    public Url(
            @JsonProperty("id") String id,
            @JsonProperty("longUrl") String longUrl,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("lastVisitedAt") Instant lastVisitedAt,
            @JsonProperty("visitCount") int visitCount) {
        this.id = id;
        this.longUrl = longUrl;
        this.createdAt = createdAt;
        this.lastVisitedAt = lastVisitedAt;
        this.visitCount = visitCount;
    }
}
