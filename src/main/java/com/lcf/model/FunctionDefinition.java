package com.lcf.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("functiondefinition")
@Getter
@Setter
public class FunctionDefinition {
    @Id
    private String id;
    private String entityAlias;
    private String functionName;
    private FunctionType functionType;
    private List<String> statement;

    public static enum FunctionType{
        SPEL,
        GROOVY,
        DRL
    }
}
