package org.javers.json;

import org.javers.common.collections.Defaults;
import org.javers.common.collections.Optional;
import org.javers.common.validation.Validate;
import org.javers.core.metamodel.object.CdoWrapper;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.ManagedType;

import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;

public class JsonCdo extends CdoWrapper {
    private final Map<String, Object> properties;
    private final Object wrappedCdo;
    public JsonCdo(Object wrappedCdo, GlobalId globalId, JsonManagedType jsonManagedType, Map<String, Object> properties) {
        super(wrappedCdo, globalId, jsonManagedType);
        this.properties = properties;
        this.wrappedCdo = wrappedCdo;
    }
    @Override
    public Optional<Object> getWrappedCdo() {
        return Optional.of(wrappedCdo);
    }

    public Map<String,Object> getProperties(){
        return properties;
    }
    @Override
    public Object getPropertyValue(Property property) {
        Validate.argumentIsNotNull(property);
        Object val = properties.get(property.getName());
        if (val == null) {
            return Defaults.defaultValue(property.getGenericType());
        }
        return val;
    }

    @Override
    public Object getPropertyValue(String propertyName) {
       // JsonProperty property = (JsonProperty) getManagedType().getProperty(propertyName);
        Object ob = properties.get(propertyName);
        //JsonProperty jsonProperty = new JsonProperty((String) properties.get(propertyName));
        //return property.get(wrappedCdo);
        return ob;
    }

    @Override
    public boolean isNull(Property property) {
        return property.isNull(wrappedCdo);
    }
}
