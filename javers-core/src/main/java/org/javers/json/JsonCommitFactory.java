package org.javers.json;

import org.javers.common.collections.Lists;
import org.javers.common.collections.Optional;
import org.javers.common.date.DateProvider;
import org.javers.common.exception.JaversException;
import org.javers.common.exception.JaversExceptionCode;
import org.javers.common.validation.Validate;
import org.javers.core.commit.*;
import org.javers.core.diff.Diff;
import org.javers.core.diff.DiffFactory;
import org.javers.core.graph.LiveGraph;
import org.javers.core.graph.LiveGraphFactory;
import org.javers.core.metamodel.object.Cdo;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.SnapshotFactory;
import org.javers.core.snapshot.GraphSnapshotFacade;
import org.javers.core.snapshot.ShadowGraph;
import org.javers.repository.api.JaversExtendedRepository;

import java.time.Instant;
import java.util.List;

/**
 * Created by Romcikas on 3/17/2016.
 */
public class JsonCommitFactory extends CommitFactory {

    private JaversExtendedRepository javersRepository;
    private SnapshotFactory snapshotFactory;
    private DiffFactory diffFactory;
    private JsonLiveGraphFactory jsonLiveGraphFactory;
    private GraphSnapshotFacade graphSnapshotFacade;
    private CommitSeqGenerator commitSeqGenerator;
    private DateProvider dateProvider;

    public JsonCommitFactory(DiffFactory diffFactory, JaversExtendedRepository javersRepository, CommitSeqGenerator commitSeqGenerator, DateProvider dateProvider, GraphSnapshotFacade graphSnapshotFacade, JsonLiveGraphFactory jsonLiveGraphFactory, SnapshotFactory snapshotFactory) {
        super(diffFactory, javersRepository, commitSeqGenerator, dateProvider, graphSnapshotFacade, jsonLiveGraphFactory, snapshotFactory);
        this.javersRepository = javersRepository;
        this.snapshotFactory = snapshotFactory;
        this.diffFactory = diffFactory;
        this.jsonLiveGraphFactory = jsonLiveGraphFactory;
        this.graphSnapshotFacade = graphSnapshotFacade;
        this.commitSeqGenerator = commitSeqGenerator;
        this.dateProvider = dateProvider;
    }

    public Commit createTerminalByGlobalId(String author, GlobalId removedId) {
        Validate.argumentsAreNotNull(author, removedId);

        Optional<CdoSnapshot> previousSnapshot = javersRepository.getLatest(removedId);
        if (previousSnapshot.isEmpty()) {
            throw new JaversException(JaversExceptionCode.CANT_DELETE_OBJECT_NOT_FOUND, removedId.value());
        }

        CommitMetadata commitMetadata = nextCommit(author);

        CdoSnapshot terminalSnapshot =
                snapshotFactory.createTerminal(removedId, previousSnapshot.get(), commitMetadata);

        Diff diff = diffFactory.singleTerminal(removedId, commitMetadata);

        return new Commit(commitMetadata, Lists.asList(terminalSnapshot), diff);
    }

    public Commit createTerminal(String author, Object removed) {
        Validate.argumentsAreNotNull(author, removed);

        Cdo removedCdo = jsonLiveGraphFactory.createCdo(removed);

        return createTerminalByGlobalId(author, removedCdo.getGlobalId());
    }

    public Commit create(String author, Object currentVersion) {
        Validate.argumentsAreNotNull(author, currentVersion);

        CommitMetadata commitMetadata = nextCommit(author);

        LiveGraph currentGraph = jsonLiveGraphFactory.createLiveGraph(currentVersion);
        ShadowGraph latestShadowGraph = graphSnapshotFacade.createLatestShadow(currentGraph);

        //capture current state
        List<CdoSnapshot> snapshots =
                graphSnapshotFacade.createGraphSnapshot(currentGraph, latestShadowGraph, commitMetadata);

        //do diff
        Diff diff = diffFactory.create(latestShadowGraph, currentGraph, Optional.of(commitMetadata));

        return new Commit(commitMetadata, snapshots, diff);
    }

    private CommitMetadata nextCommit(String author) {
        CommitId head = javersRepository.getHeadId();
        CommitId newId = commitSeqGenerator.nextId(head);

        return new CommitMetadata(author, dateProvider.now(), newId);
    }
}
