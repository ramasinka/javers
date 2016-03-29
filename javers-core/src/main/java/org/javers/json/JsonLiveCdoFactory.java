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
    private Map<Object, Object> map;

    public JsonLiveCdoFactory(GlobalIdFactory globalIdFactory, ObjectAccessHook objectAccessHook, TypeMapper typeMapper) {
        super(globalIdFactory, objectAccessHook, typeMapper);
        this.globalIdFactory = globalIdFactory;
        this.objectAccessHook = objectAccessHook;
        this.typeMapper = typeMapper;
    }

    @Override
    public Cdo create(Object jsonCdo, OwnerContext owner) {
        Object jsonCdoAccessed = objectAccessHook.access(jsonCdo);
        map = ((JsonLiveGraphFactory.MapWrapper) jsonCdoAccessed).getMap();
        GlobalId globalId = globalIdFactory.createId(jsonCdoAccessed, owner);
        return new JsonCdo(jsonCdo, globalId, typeMapper.getJaversManagedType(jsonCdoAccessed.getClass()),map);
    }
}
