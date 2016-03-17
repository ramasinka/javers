package org.javers.spring.auditable.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.javers.common.collections.Lists
import org.javers.spring.repository.jpa.DummyAuditedJpaCrudRepository
import org.javers.spring.model.DummyObject
import spock.lang.Specification

/**
 * Created by gessnerfl on 22.02.15.
 */
class JaversAuditableRepositoryAspectTest extends Specification {

    def saveHandler = Mock(AuditChangeHandler.class);
    def deleteHandler = Mock(AuditChangeHandler.class)

    def changedObject = Mock(DummyObject.class);
    def pjp = Mock(ProceedingJoinPoint.class)

    def sut = new JaversAuditableRepositoryAspect(saveHandler, deleteHandler, Mock(JaversCommitAdvice))

    def "Should trigger save handler for single object"(){
        setup:
        initRepository()
        pjp.getArgs() >> [changedObject]

        when:
        sut.onSaveExecuted(pjp)

        then:
        1 * saveHandler.handle(_, changedObject)
    }

    def "Should trigger save handler for multiple objects"(){
        setup:
        initRepository()
        def changes = Lists.asList(changedObject, changedObject)
        pjp.getArgs() >> [changes]

        when:
        sut.onSaveExecuted(pjp)

        then:
        2 * saveHandler.handle(_, changedObject)
    }

    def "Should trigger delete handler for single object"(){
        setup:
        initRepository()
        pjp.getArgs() >> [changedObject]

        when:
        sut.onDeleteExecuted(pjp)

        then:
        1 * deleteHandler.handle(_, changedObject)
    }

    def "Should trigger delete handler for multiple objects"(){
        setup:
        initRepository()
        def changes = Lists.asList(changedObject, changedObject)
        pjp.getArgs() >> [changes]

        when:
        sut.onDeleteExecuted(pjp)

        then:
        2 * deleteHandler.handle(_, changedObject)
    }

    def initRepository(){
        def repo = Mock(DummyAuditedJpaCrudRepository.class)
        pjp.getTarget() >> repo
    }
}
