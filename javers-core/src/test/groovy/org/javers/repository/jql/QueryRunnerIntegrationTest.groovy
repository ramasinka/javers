package org.javers.repository.jql

import org.javers.core.JaversTestBuilder
import org.javers.core.examples.typeNames.NewEntityWithTypeAlias
import org.javers.core.examples.typeNames.NewValueObjectWithTypeAlias
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author bartosz.walacik
 */
class QueryRunnerIntegrationTest extends Specification {

    @Unroll
    def "should touch Entity and ValueObject classes before running #queryType query"(){
        given:
        def javers = JaversTestBuilder.javersTestAssembly()
        def queryRunner = javers.queryRunner
        def typeMapper = javers.typeMapper

        when:
        action.call(queryRunner, query)

        then:
        expectedMappedTypes.each {
            assert typeMapper.getJaversManagedType(it)
        }

        where:
        query << [QueryBuilder.byInstanceId(1, NewEntityWithTypeAlias).build(),
                  QueryBuilder.byInstanceId(1, NewEntityWithTypeAlias).andProperty("id").build(),
                  QueryBuilder.byClass(NewEntityWithTypeAlias).build(),
                  QueryBuilder.byClass(NewValueObjectWithTypeAlias).build(),
                  QueryBuilder.byValueObject(NewEntityWithTypeAlias,"valueObject").build(),
                  QueryBuilder.byValueObjectId(1, NewEntityWithTypeAlias,"valueObject").build()
                ] * 2
        expectedMappedTypes << [
                ["myName"],
                ["myName"],
                ["myName"],
                ["myValueObject"],
                ["myName", "myValueObject"],
                ["myName", "myValueObject"]
        ] * 2
        queryType << ["byInstanceId",
                      "byInstanceId and Property",
                      "Entity byClass",
                      "ValueObject byClass",
                      "byValueObject",
                      "byValueObjectId"
        ] * 2
        action << [ { runner, query -> runner.queryForChanges(query) } ] * 6 +
                  [ { runner, query -> runner.queryForSnapshots(query) } ] * 6


    }
}
