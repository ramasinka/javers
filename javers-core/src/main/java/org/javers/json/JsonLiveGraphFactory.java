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
    private CollectionsCdoFactory collectionsCdoFactory;

    public JsonLiveGraphFactory(TypeMapper typeMapper, JsonLiveCdoFactory jsonLiveCdoFactory, CollectionsCdoFactory collectionsCdoFactory) {
        super(typeMapper, jsonLiveCdoFactory, collectionsCdoFactory);
        this.jsonLiveCdoFactory = jsonLiveCdoFactory;
        this.typeMapper = typeMapper;
        this.collectionsCdoFactory = collectionsCdoFactory;
    }
    public LiveGraph createLiveGraph(Collection handle, Class clazz) {
        CollectionWrapper wrappedCollection = (CollectionWrapper) wrapTopLevelContainer(handle);

        return new CollectionsGraphBuilder(new ObjectGraphBuilder(typeMapper, jsonLiveCdoFactory), collectionsCdoFactory)
                .buildGraph(wrappedCollection, clazz);
    }

    /**
     * delegates to {@link ObjectGraphBuilder#buildGraph(Object)}
     */
    public LiveGraph createLiveGraph(Object handle) {
        Object wrappedHandle = wrapTopLevelContainer(handle);

        return new ObjectGraphBuilder(typeMapper, jsonLiveCdoFactory).buildGraph(wrappedHandle);
    }

    public Cdo createCdo(Object cdo){
        return jsonLiveCdoFactory.create(cdo, null);
    }

    public Object wrapTopLevelContainer(Object handle){
        if (handle instanceof  Map){
            return new MapWrapper((Map)handle);
        }
        if (handle instanceof  List){
            return new ListWrapper((List)handle);
        }
        if (handle instanceof  Set){
            return new SetWrapper((Set)handle);
        }
        if (handle.getClass().isArray()){
            return new ArrayWrapper(convertToObjectArray(handle));
        }
        return handle;
    }

    public static Class getMapWrapperType(){
        return MapWrapper.class;
    }

    public static Class getSetWrapperType(){
        return SetWrapper.class;
    }

    public static Class getListWrapperType(){
        return ListWrapper.class;
    }

    public static Class getArrayWrapperType() {
        return ArrayWrapper.class;
    }

      public static class MapWrapper {
         private Map<String,Object> properties;

        MapWrapper(Map<String ,Object> map) {
            this.properties = map;
        }
        public Map<String,Object> getMap(){
            return properties;
        }
    }


    static class SetWrapper implements CollectionWrapper {
        private final Set<Object> set;

        SetWrapper(Set set) {
            this.set = set;
        }

        Set<Object> getSet() {
            return set;
        }
    }

    static class ListWrapper implements CollectionWrapper {
        private final List<Object> list;

        ListWrapper(List list) {
            this.list = list;
        }

        List<Object> getList() {
            return list;
        }
    }

    static class ArrayWrapper {
        private final Object[] array;

        ArrayWrapper(Object[] objects) {
            this.array = objects;
        }
    }

    //this is primarily used for copying array of primitives to array of objects
    //as there seems be no legal way for casting
    private static Object[] convertToObjectArray(Object obj) {
        if (obj instanceof Object[]) {
            return (Object[]) obj;
        }
        int arrayLength = Array.getLength(obj);
        Object[] retArray = new Object[arrayLength];
        for (int i = 0; i < arrayLength; ++i){
            retArray[i] = Array.get(obj, i);
        }
        return retArray;
    }

}