package org.javers.json;

import org.javers.core.graph.LiveCdoFactory;
import org.javers.core.graph.ObjectAccessHook;
import org.javers.core.metamodel.object.*;
import org.javers.core.metamodel.type.TypeMapper;

import java.util.Map;


public class JsonLiveCdoFactory extends LiveCdoFactory {
    private GlobalIdFactory globalIdFactory;
    private ObjectAccessHook objectAccessHook;
    private TypeMapper typeMapper;

    public JsonLiveCdoFactory(GlobalIdFactory globalIdFactory, ObjectAccessHook objectAccessHook, TypeMapper typeMapper) {
        super(globalIdFactory, objectAccessHook, typeMapper);
    }

    @Override
    public Cdo create(Object jsonCdo, OwnerContext owner) {
        Map<String, Object> properties = null;
        Object jsonCdoAccessed = objectAccessHook.access(jsonCdo);
        GlobalId globalId = globalIdFactory.createId(jsonCdoAccessed, owner);
        return new JsonCdo(globalId, typeMapper.getJaversManagedType(jsonCdoAccessed.getClass()),properties);
    }
}
