package com.lcf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcf.model.EntityDefinition;
import com.lcf.model.FunctionDefinition;
import com.lcf.service.EntityManagementServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntityManagementController {

    @Autowired
    private EntityManagementServices entityManagementServices;

    private final ObjectMapper om = new ObjectMapper();



    @PostMapping("/entity/definition")
    public EntityDefinition createEntityDefinition(@RequestBody EntityDefinition ed){
        return entityManagementServices.createEntityDefiniton(ed);
    }

    @PostMapping("/function/definition")
    public FunctionDefinition createFunctionDefinition(@RequestBody FunctionDefinition fd){
        return entityManagementServices.createFunctionDefiniton(fd);
    }

    @PostMapping("/entity/{alias}")
    public String createEntity(@PathVariable(name = "alias") String alias, @RequestBody String payload) throws Exception{
        return om.writeValueAsString(entityManagementServices.createEntity(alias,payload));
    }

    @GetMapping("/entity/{alias}/{id}")
    public String getEntity(@PathVariable(name = "alias") String alias, @PathVariable(name = "id") String id) throws Exception{
        return om.writeValueAsString(entityManagementServices.getEntity(alias,id));
    }

    @PutMapping("/entity/{alias}/{id}")
    public String updateEntity(@PathVariable(name = "alias") String alias, @PathVariable(name = "id") String id,@RequestBody String payload) throws Exception{
        return om.writeValueAsString(entityManagementServices.updateEntity(alias,id,payload));
    }
    @DeleteMapping("/entity/{alias}/{id}")
    public Boolean deleteEntity(@PathVariable(name = "alias") String alias, @PathVariable(name = "id") String id) throws Exception{
        return entityManagementServices.deleteEntity(alias,id);
    }

    @PostMapping("/search/{alias}")
    public String searchEntity(@PathVariable(name = "alias") String alias, @RequestBody String searchQuery) throws Exception{
        return om.writeValueAsString(entityManagementServices.searchEntity(alias,searchQuery));
    }

    @GetMapping("/functionexec/{alias}/{id}/{functionName}")
    public String executeFunction(@PathVariable(name = "alias") String alias,
                                  @PathVariable(name = "id") String id,
                                  @PathVariable(name = "functionName") String functionName) throws Exception{
        return om.writeValueAsString(entityManagementServices.executeFunction(alias,id,functionName));
    }

}
