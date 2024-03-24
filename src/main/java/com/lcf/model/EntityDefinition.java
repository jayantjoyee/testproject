package com.lcf.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("entitydefinition")
@Getter @Setter
public class EntityDefinition {
    @Id
    private String id;

    private String alias;

    private String nameSpace;

    private List<Field> fields;
}
