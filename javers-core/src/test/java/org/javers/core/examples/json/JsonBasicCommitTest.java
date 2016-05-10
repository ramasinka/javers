package org.javers.core.examples.json;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.ArrayChange;
import org.javers.core.diff.changetype.container.ListChange;
import org.javers.core.diff.changetype.map.MapChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class JsonBasicCommitTest {

    private Javers javers;
    private Map<String, Object> jsonEntity;
    private String testEntityType;

    @Before
    public void init() {
        javers = JaversBuilder.javers().build();

        jsonEntity = new HashMap<>();
        jsonEntity.put("id", "jsonEntity");
        jsonEntity.put("name", "test entity");
        Map<String, Object> links = new HashMap<>();
        Map<String, Object> selfLink = new HashMap<>();
        testEntityType = "TestEntity";
        selfLink.put("classType", testEntityType);
        links.put("self", selfLink);
        jsonEntity.put("_links", links);
    }

    @Test
    public void shouldCommitToJaversRepository() {
        javers.commit("user", jsonEntity);

        jsonEntity.put("name", "new name");

        javers.commit("user", jsonEntity);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());
        assertThat(snapshots).hasSize(2);
    }

    @Test
    public void shouldListStateHistory() {
        String nameOld = (String) jsonEntity.get("name");
        String nameNew = "new name";

        javers.commit("user", jsonEntity);

        jsonEntity.put("name", nameNew);

        javers.commit("user", jsonEntity);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).limit(10).build());

        assertThat(snapshots).hasSize(2);
        CdoSnapshot newState = snapshots.get(0);
        CdoSnapshot oldState = snapshots.get(1);
        assertThat(oldState.getPropertyValue("name")).isEqualTo(nameOld);
        assertThat(newState.getPropertyValue("name")).isEqualTo(nameNew);
    }

    @Test
    public void shouldListChangeHistory() {
        String nameOld = (String) jsonEntity.get("name");
        javers.commit("user", jsonEntity);

        String nameNew = "new name";
        jsonEntity.put("name", nameNew);

        javers.commit("user", jsonEntity);

        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ValueChange change = (ValueChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("name");
        assertThat(change.getLeft()).isEqualTo(nameOld);
        assertThat(change.getRight()).isEqualTo(nameNew);

    }

    @Test
    public void shouldFindChangesWhenAddInt() {
        int age = 23;

        javers.commit("user", jsonEntity);
        jsonEntity.put("age", age);
        javers.commit("user", jsonEntity);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).limit(10).build());

        assertThat(snapshots).hasSize(2);
        CdoSnapshot ageState = snapshots.get(0);
        assertThat(ageState.getPropertyValue("age")).isEqualTo(age);
        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ValueChange change = (ValueChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("age");
        assertThat(change.getRight()).isEqualTo(age);

    }

    @Test
    public void shouldFindChangesWhenAddDouble() {
        double weight = 82.78;
        javers.commit("user", jsonEntity);

        jsonEntity.put("weight", weight);
        javers.commit("user", jsonEntity);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).limit(10).build());
        assertThat(snapshots).hasSize(2);
        CdoSnapshot weightState = snapshots.get(0);
        assertThat(weightState.getPropertyValue("weight")).isEqualTo(weight);

        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ValueChange change = (ValueChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("weight");
        assertThat(change.getRight()).isEqualTo(weight);

    }

    @Test
    public void shouldFindChangesWhenAddArray() {
        String[] friends = {"John", "Rom", "Antonio", "Patric"};

        javers.commit("user", jsonEntity);
        jsonEntity.put("friends", friends);
        javers.commit("user", jsonEntity);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).limit(10).build());
        assertThat(snapshots).hasSize(2);
        CdoSnapshot friendsState = snapshots.get(0);
        assertThat(friendsState.getPropertyValue("friends")).isEqualTo(friends);

        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ArrayChange change = (ArrayChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("friends");
    }
    @Test
    public void shouldFindChangesWhenAddArrayListWithString() {
        List<String> arrlist = new ArrayList<String>();
        arrlist.add("test1");
        arrlist.add("test2");
        javers.commit("user", jsonEntity);
        jsonEntity.put("arrlist", arrlist);
        javers.commit("user", jsonEntity);
        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ListChange change = (ListChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("arrlist");
    }
    @Test
    public void shouldFindChangesWhenAddArrayListWithDouble() {
        List<Double> arrlist = new ArrayList<Double>();
        arrlist.add(1.00);
        arrlist.add(2.00);
        javers.commit("user", jsonEntity);
        jsonEntity.put("arrlist", arrlist);
        javers.commit("user", jsonEntity);
        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ListChange change = (ListChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("arrlist");
    }
    @Test
    public void shouldFindChangesWhenAddArrayListWithInteger() {
        List<Integer> arrlist = new ArrayList<Integer>();
        arrlist.add(10);
        arrlist.add(20);
        javers.commit("user", jsonEntity);
        jsonEntity.put("arrlist", arrlist);
        javers.commit("user", jsonEntity);
        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());

        assertThat(changes).hasSize(1);
        ListChange change = (ListChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("arrlist");
    }

    @Test
    public void shouldFindChangesWhenAddMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("test","test");
        javers.commit("user", jsonEntity);
        jsonEntity.put("testMap", map);
        javers.commit("user", jsonEntity);

        List<Change> changes = javers.findChanges(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());
        assertThat(changes).hasSize(1);
        MapChange change = (MapChange) changes.get(0);
        assertThat(change.getPropertyName()).isEqualTo("testMap");
    }

    @Test
    public void shouldFindChangeWhenRemoveMap() {
        javers.commit("user", jsonEntity);
        assertThat(jsonEntity).hasSize(3);
        jsonEntity.remove("name");
        javers.commit("user", jsonEntity);
        assertThat(jsonEntity).hasSize(2);

        List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byJsonInstanceId(jsonEntity.get("id"), testEntityType).build());
        assertThat(snapshots).hasSize(2);
    }
}
