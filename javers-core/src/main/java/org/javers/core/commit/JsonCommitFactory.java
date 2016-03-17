package org.javers.core.commit;

import org.javers.common.date.DateProvider;
import org.javers.core.diff.DiffFactory;
import org.javers.core.graph.LiveGraphFactory;
import org.javers.core.metamodel.object.SnapshotFactory;
import org.javers.core.snapshot.GraphSnapshotFacade;
import org.javers.repository.api.JaversExtendedRepository;

/**
 * Created by Romcikas on 3/17/2016.
 */
public class JsonCommitFactory extends CommitFactory {

    public JsonCommitFactory(DiffFactory diffFactory, JaversExtendedRepository javersRepository, CommitSeqGenerator commitSeqGenerator, DateProvider dateProvider, GraphSnapshotFacade graphSnapshotFacade, LiveGraphFactory liveGraphFactory, SnapshotFactory snapshotFactory) {
        super(diffFactory, javersRepository, commitSeqGenerator, dateProvider, graphSnapshotFacade, liveGraphFactory, snapshotFactory);
    }
}
