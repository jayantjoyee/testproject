package com.lcf.service;


import com.lcf.model.FunctionDefinition;

public interface FunctionProcessor {
    public <T> void execute(String alias, T entity, FunctionDefinition fd);
}
