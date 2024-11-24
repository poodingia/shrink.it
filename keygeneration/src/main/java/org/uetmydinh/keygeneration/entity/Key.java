package org.uetmydinh.keygeneration.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keys")
@Data
@AllArgsConstructor
public class Key {
    @Id
    private String id;

    @Indexed
    private Boolean isUsed;
}
