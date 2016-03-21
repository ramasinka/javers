package org.javers.json;


import org.javers.core.graph.*;
import org.javers.core.metamodel.object.Cdo;
import org.javers.core.metamodel.type.TypeMapper;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JsonLiveGraphFactory extends LiveGraphFactory {

    private JsonLiveCdoFactory jsonLiveCdoFactory;
    private TypeMapper typeMapper;

    public JsonLiveGraphFactory(TypeMapper typeMapper, JsonLiveCdoFactory jsonLiveCdoFactory, CollectionsCdoFactory collectionsCdoFactory) {
        super(typeMapper, jsonLiveCdoFactory, collectionsCdoFactory);
        this.jsonLiveCdoFactory = jsonLiveCdoFactory;
        this.typeMapper = typeMapper;
    }
    public Cdo createCdo(Object cdo){
        return jsonLiveCdoFactory.create(cdo, null);
    }
    public LiveGraph createLiveGraph(Object handle) {
        Object wrappedHandle = wrapTopLevelContainer(handle);

        return new ObjectGraphBuilder(typeMapper, jsonLiveCdoFactory).buildGraph(wrappedHandle);
    }
}