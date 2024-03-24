package com.lcf.service;

import com.lcf.model.FunctionDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SPELFunctionProcessor implements FunctionProcessor{
    @Override
    public <T> void execute(T entity, FunctionDefinition fd) {
        fd.getStatement().forEach(stmt -> {
            ExpressionParser expressionParser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext(entity);
            Expression expression = expressionParser.parseExpression(stmt);
            expression.getValue(context);
        });
    }
}
