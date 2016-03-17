package org.javers.core.examples.json;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Before;
import org.junit.Test;

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
}
