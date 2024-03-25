package com.lcf.service;

import com.lcf.model.FunctionDefinition;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DroolsFunctionProcessor implements FunctionProcessor{

    private final Map<String, KieBase> ruleContainerMap = new HashMap<>();

    @Override
    public <T> void execute(String alias, T entity, FunctionDefinition fd) {
        KieBase kieBase = getKieBase(alias,fd);
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(entity);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

    private KieBase getKieBase(String alias, FunctionDefinition fd){
        String containerKey = alias + "~"+fd.getFunctionName();
        return ruleContainerMap.computeIfAbsent(containerKey, ck -> {
            KieServices kieServices = KieServices.Factory.get();
            KieHelper kieHelper = new KieHelper();
            byte[] b1 = fd.getStatement().get(0).getBytes();
            Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
            kieHelper.addResource(resource1, ResourceType.DRL);
            KieBase kieBase = kieHelper.build();
            return null;
        });
    }

}
