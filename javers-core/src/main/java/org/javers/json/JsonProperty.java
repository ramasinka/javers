package org.javers.json;

import org.javers.common.reflection.JaversMember;
import org.javers.core.metamodel.property.Property;

import java.lang.reflect.Type;

/**
 * Created by Romcikas on 4/26/2016.
 */
public class JsonProperty extends Property {
    String name;
    Type jsonType;

    public JsonProperty(JaversMember member, boolean hasTransientAnn) {
        super(member, hasTransientAnn);
    }

    public JsonProperty(String name, Type jsonType) {
        this.name = name;
        this.jsonType = jsonType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getGenericType() {
        return jsonType;
    }
}