package org.javers.json;

import org.javers.common.validation.Validate;
import org.javers.repository.jql.GlobalIdDTO;


public final class JsonInstanceIdDTO extends GlobalIdDTO {
    private String entityType = null;
    private final Object localId;

    JsonInstanceIdDTO(String entityType, Object localId) {
        Validate.argumentsAreNotNull(entityType, localId);
        this.entityType = entityType;
        this.localId = localId;
    }

    public static JsonInstanceIdDTO instanceId(Object localId, String entityType){
        Validate.argumentsAreNotNull(localId, entityType);
        return new JsonInstanceIdDTO(entityType, localId);
    }

    @Override
    public String value() {
        return entityType+"/"+localId;
    }


    public Object getCdoId() {
        return localId;
    }

    public String getEntityType() {
        return entityType;
    }
}
