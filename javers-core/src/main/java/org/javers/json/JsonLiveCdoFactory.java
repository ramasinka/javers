package org.javers.json;

import org.javers.core.graph.LiveCdoFactory;
import org.javers.core.graph.ObjectAccessHook;
import org.javers.core.metamodel.object.*;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.ManagedClass;
import org.javers.core.metamodel.type.TypeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JsonLiveCdoFactory extends LiveCdoFactory {
    private GlobalIdFactory globalIdFactory;
    private ObjectAccessHook objectAccessHook;
    private TypeMapper typeMapper;
    private JsonManagedType jsonManagedType;
    private Map<String, Object> map;
    private ManagedClass managedClass;
    private List<Property> managedProperties = new ArrayList<>();
    private List<Property> looksLikeId = new ArrayList<>();

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
        managedClass = new ManagedClass(jsonCdo.getClass(),managedProperties,looksLikeId);
        GlobalId globalId = globalIdFactory.createId(jsonCdoAccessed, owner);
        jsonManagedType = new JsonManagedType(managedClass, jsonCdoAccessed);
        return new JsonCdo(jsonCdo, globalId, jsonManagedType, map);
    }
}
