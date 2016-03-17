package org.javers.core.metamodel.object;

import org.javers.common.collections.Defaults;
import org.javers.common.collections.Optional;
import org.javers.common.validation.Validate;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.ManagedType;

import java.util.List;
import java.util.Map;

/**
 * Created by Romcikas on 3/17/2016.
 */
public class JsonCdo extends Cdo {
    private final Map<String, Object> properties;

    public JsonCdo(GlobalId globalId, ManagedType managedType, Map<String, Object> properties) {
        super(globalId, managedType);
        this.properties = properties;
        Validate.argumentsAreNotNull(properties, managedType);
    }

    @Override
    public Optional<Object> getWrappedCdo() {
        return null;
    }

    @Override
    public boolean isNull(Property property) {
        return false;
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
        Validate.argumentIsNotNull(propertyName);
        return properties.get(propertyName);
    }

    public Object getPropertyId(int propertyId) {
        Validate.argumentIsNotNull(propertyId);
        return properties.get(propertyId);
    }

}
