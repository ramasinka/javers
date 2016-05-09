package org.javers.json;

import org.javers.common.reflection.JaversMember;
import org.javers.core.metamodel.property.Property;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Romcikas on 4/26/2016.
 */
public class JsonProperty extends Property {
    String name;
    Type jsonType;
     Map<String, Object> map = new HashMap<String, Object>();

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
           try {
                Type type = map.getClass();
               if(jsonType.equals(type) ){
                   ParameterizedType pt = (ParameterizedType) JsonProperty.class.getDeclaredField("map").getGenericType();
                   return pt;
               }
           } catch (NoSuchFieldException e) {
               e.printStackTrace();
           }
        return jsonType;
    }
}