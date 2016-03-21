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

    public Cdo create(Map<String, Object> properties, OwnerContext owner) {
        Object jsonCdoAccessed = objectAccessHook.access(properties);
        GlobalId globalId = globalIdFactory.createId(jsonCdoAccessed, owner);
        return new JsonCdo(properties,globalId,typeMapper.getJaversManagedType(properties.getClass()));
    }
}
