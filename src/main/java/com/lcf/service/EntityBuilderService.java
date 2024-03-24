package com.lcf.service;

import com.lcf.model.BaseModel;
import com.lcf.model.EntityDefinition;
import com.lcf.model.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntityBuilderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Map<String,Class<?>> classDefinition = new HashMap<>();

    private ObjectMapper om = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> T getInstance(String alias) throws Exception{
        Query query = new Query();
        query.addCriteria(Criteria.where("alias").is(alias));
        EntityDefinition entityDefinition = mongoTemplate.findOne(query,EntityDefinition.class);
        String nameSpace = (entityDefinition.getNameSpace() != null) ?entityDefinition.getNameSpace():"com.lcf.model";
        String className = nameSpace+"."+alias;
        Class<?> clazz = classDefinition.computeIfAbsent(alias,s -> {
            DynamicType.Builder<BaseModel> builder = new ByteBuddy()
                    .subclass(BaseModel.class)
                    .name(className);
            for(Field fld : entityDefinition.getFields()){
                builder = builder.defineProperty(fld.getName(), String.class);
            }
            return builder.make().load(Thread.currentThread().getContextClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
        });
        return (T) clazz.getDeclaredConstructors()[0].newInstance(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> T getInstance(String alias,String payload) throws Exception{
        T instance = getInstance(alias);
        Class<T> clazz = (Class<T>)instance.getClass();
        return om.readValue(payload,clazz);
    }
}
