package com.lcf.service;

import com.lcf.model.BaseModel;
import com.lcf.model.EntityDefinition;
import com.lcf.model.FunctionDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EntityManagementServices {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private EntityBuilderService entityBuilderService;

    @Autowired
    private GroovyFunctionProcessor groovyFunctionProcessor;

    @Autowired
    private SPELFunctionProcessor spelFunctionProcessor;

    @Autowired
    private DroolsFunctionProcessor droolsFunctionProcessor;

    public EntityDefinition createEntityDefiniton(EntityDefinition ed){
        return mongoTemplate.insert(ed);
    }

    public FunctionDefinition createFunctionDefiniton(FunctionDefinition fd){
        return mongoTemplate.insert(fd);
    }

    public <T extends BaseModel> T createEntity(String alias, String payload) throws Exception{
        T instance = entityBuilderService.getInstance(alias,payload);
        mongoTemplate.createCollection(alias);
        return mongoTemplate.insert(instance,alias);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> T getEntity(String alias, String id) throws Exception{
        T instance = entityBuilderService.getInstance(alias);
        return (T)mongoTemplate.findById(id,instance.getClass());
    }

    public <T extends BaseModel> T updateEntity(String alias, String id, String payload) throws Exception{
        T instance = entityBuilderService.getInstance(alias,payload);
        instance.set_id(id);
        return mongoTemplate.save(instance);
    }

    public <T extends BaseModel> boolean deleteEntity(String alias, String id) throws Exception{
        T instance = entityBuilderService.getInstance(alias);
        instance.set_id(id);
        mongoTemplate.remove(instance,alias);
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> List<T> searchEntity(String alias, String searchQuery) throws Exception{
        T instance = entityBuilderService.getInstance(alias);
        searchQuery = "{T(org.springframework.data.mongodb.core.query.Criteria)."+searchQuery+"}";
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(searchQuery);
        List<Criteria> criteria = (List<Criteria>) expression.getValue();
        Query query = new Query();
        criteria.stream().forEach(c -> query.addCriteria(c));
        return (List<T>)mongoTemplate.find(query,instance.getClass(),alias);
    }

    public <T extends BaseModel> T executeFunction(String alias, String id, String functionName) throws Exception{
        Query query = new Query();
        query.addCriteria(Criteria.where("entityAlias").is(alias).and("functionName").is(functionName));
        FunctionDefinition functionDefinition = mongoTemplate.findOne(query,FunctionDefinition.class);
        T instance = getEntity(alias,id);
        getFunctionProcessor(functionDefinition).execute(alias,instance,functionDefinition);
        return instance;
    }

    private FunctionProcessor getFunctionProcessor(FunctionDefinition fd){
        if(fd.getFunctionType() == FunctionDefinition.FunctionType.SPEL)
            return spelFunctionProcessor;
        else if(fd.getFunctionType() == FunctionDefinition.FunctionType.GROOVY)
            return groovyFunctionProcessor;
        else if(fd.getFunctionType() == FunctionDefinition.FunctionType.DROOLS)
            return droolsFunctionProcessor;
        return null;
    }

}
