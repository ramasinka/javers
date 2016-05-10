package org.javers.json;

import org.javers.common.collections.Predicate;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.ManagedClass;
import org.javers.core.metamodel.type.ManagedType;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Romcikas on 4/13/2016.
 */
public class JsonManagedType extends ManagedType {
    private Object jsonCdo;
    private ManagedClass managedClass;
    private Map<String, Object> jsonMap;
    private JsonProperty jsonProperty;


    public JsonManagedType(ManagedClass managedClass, Object jsonCdo) {
        super(managedClass);
        this.jsonCdo = (JsonLiveGraphFactory.MapWrapper) jsonCdo;
        this.managedClass = managedClass;
        jsonMap = (HashMap) ((JsonLiveGraphFactory.MapWrapper) jsonCdo).getMap();

    }

    @Override
    public Property getProperty(String propertyName) {
        for (Object key : jsonMap.keySet()) {
            if (key == propertyName) {
                return new JsonProperty((String) key, jsonMap.get(key).getClass());
            }
        }
        return managedClass.getProperty(propertyName);
    }

    @Override
    public List<Property> getProperties(Predicate<Property> query) {
        return managedClass.getManagedProperties(query);
    }

    @Override
    public List<Property> getProperties() {
        List<Property> properties = new ArrayList<>();
        for (Object key : jsonMap.keySet()) {
            properties.add(getProperty((String) key));
        }
        return properties;
    }

    @Override
    public Set<String> getPropertyNames() {
        return managedClass.getPropertyNames();
    }

}
