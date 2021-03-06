package org.javers.core.commit;

import org.javers.common.collections.Lists;
import org.javers.core.pico.InstantiatingModule;
import org.javers.json.JsonCommitFactory;
import org.picocontainer.MutablePicoContainer;

import java.util.Collection;

/**
 * @author bartosz walacik
 */
public class CommitFactoryModule extends InstantiatingModule {
    public CommitFactoryModule(MutablePicoContainer container) {
        super(container);
    }

    @Override
    protected Collection<Class> getImplementations() {
        return (Collection)Lists.asList(
                //TODO Romas - replace by JsonCommitFactory
                JsonCommitFactory.class,
                CommitSeqGenerator.class
        );
    }
}
