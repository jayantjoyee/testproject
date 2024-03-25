package com.lcf.service;

import com.lcf.model.FunctionDefinition;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.stereotype.Component;

@Component
public class DroolsFunctionProcessor implements FunctionProcessor{
    @Override
    public <T> void execute(String alias, T entity, FunctionDefinition fd) {
        Binding binding = new Binding();
        binding.setProperty(alias,entity);
        fd.getStatement().forEach(stmt -> {
            GroovyShell shell = new GroovyShell(binding);
            shell.evaluate(stmt);
        });
    }
}
