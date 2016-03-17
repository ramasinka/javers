package org.javers.core.metamodel.object;

import org.javers.common.exception.JaversException;
import org.javers.common.reflection.ReflectionUtil;
import org.javers.core.metamodel.type.EntityType;

import static org.javers.common.validation.Validate.argumentsAreNotNull;

/**
 * Entity instance global identifier, consists of Entity reference and cdoId
 *
 * @author bartosz walacik
 */
public class InstanceId extends GlobalId {
    private final Object cdoId;

    InstanceId(String typeName, Object cdoId) {
        super(typeName);
        argumentsAreNotNull(cdoId);
        this.cdoId = cdoId;
    }

    /**
     * @throws JaversException ENTITY_INSTANCE_WITH_NULL_ID
     * @throws JaversException NOT_INSTANCE_OF
     */
    public static InstanceId createFromInstance(Object instance, EntityType entity){
        return new InstanceId(entity.getName(), entity.getIdOf(instance));
    }

    /**
     * Identifier of (client's) Entity <b>instance</b>, should be unique in Entity scope.
     * For example database primary key or any domain identifier like user.login
     */
    public Object getCdoId() {
        return cdoId;
    }

    public String value() {
        return getTypeName()+"/"+getCdoIdAsString();
    }

    private String getCdoIdAsString(){
        return ReflectionUtil.reflectiveToString(cdoId);
    }
}
