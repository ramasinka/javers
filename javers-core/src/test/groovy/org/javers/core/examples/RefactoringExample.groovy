package org.javers.core.examples

import org.javers.core.JaversBuilder
import org.javers.repository.jql.QueryBuilder
import spock.lang.Specification

/**
 * @author bartosz.walacik
 */
class RefactoringExample extends Specification {

    def '''should allow Entity class name change
           when both old and new class use @TypeName annotation'''()
    {
        given:
        def javers = JaversBuilder.javers().build()
        javers.commit('author', new Person(id:1, name:'Bob'))

        when: '''Refactoring happens here, Person.class is removed,
                 new PersonRefactored.class appears'''
        javers.commit('author', new PersonRefactored(id:1, name:'Uncle Bob', city:'London'))

        def changes =
            javers.findChanges( QueryBuilder.byInstanceId(1,PersonRefactored.class).build() )

        then: 'one ValueChange is expected'
        assert changes.size() == 1
        with(changes[0]){
            assert left == 'Bob'
            assert right == 'Uncle Bob'
            assert affectedGlobalId.value() == 'Person/1'
        }
        println changes[0]
    }

    def '''should allow Entity class name change
           when old class forgot to use @TypeName annotation'''()
    {
      given:
      def javers = JaversBuilder.javers().build()
      javers.commit('author', new PersonSimple(id:1, name:'Bob'))

      when:
      javers.commit('author', new PersonRetrofitted(id:1, name:'Uncle Bob'))

      def changes =
          javers.findChanges( QueryBuilder.byInstanceId(1,PersonRetrofitted.class).build() )

      then: 'one ValueChange is expected'
      assert changes.size() == 1
      with(changes[0]){
          assert left == 'Bob'
          assert right == 'Uncle Bob'
          assert affectedGlobalId.value() == 'org.javers.core.examples.PersonSimple/1'
      }
      println changes[0]
    }

    def 'should be very relaxed about ValueObject types'(){
      given:
      def javers = JaversBuilder.javers().build()
      javers.commit('author', new Person(1,new EmailAddress('me@example.com', false)))
      javers.commit('author', new Person(1,new HomeAddress ('London','Green 50', true)))
      javers.commit('author', new Person(1,new HomeAddress ('London','Green 55', true)))

      when:
      def changes =
          javers.findChanges( QueryBuilder.byValueObjectId(1, Person.class, 'address').build() )

      then: 'three ValueChanges are expected'
      assert changes.size() == 3
      assert changes.collect{ it.propertyName }.containsAll( ['street','verified','email'] )

      changes.each { println it }
    }
}
