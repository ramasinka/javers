package org.javers.core.graph;

import org.javers.common.collections.Lists;
import org.javers.core.pico.InstantiatingModule;
import org.javers.json.JsonLiveCdoFactory;
import org.javers.json.JsonLiveGraphFactory;
import org.picocontainer.MutablePicoContainer;

import java.util.Collection;

/**
 * @author bartosz.walacik
 */
public class GraphFactoryModule extends InstantiatingModule {
    public GraphFactoryModule(MutablePicoContainer container) {
        super(container);
    }
    @Override
    protected Collection<Class> getImplementations() {
        return (Collection) Lists.asList(
               JsonLiveCdoFactory.class,
               CollectionsCdoFactory.class,
               JsonLiveGraphFactory.class,
               ObjectGraphBuilder.class,
               ObjectAccessHookDoNothingImpl.class);
    }
}
