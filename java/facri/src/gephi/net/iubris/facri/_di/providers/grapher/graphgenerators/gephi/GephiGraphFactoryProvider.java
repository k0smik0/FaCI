package net.iubris.facri._di.providers.grapher.graphgenerators.gephi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;

@Singleton
public class GephiGraphFactoryProvider implements Provider<GraphFactory> {
	
	private final GraphFactory graphFactory;

	@Inject
	public GephiGraphFactoryProvider(GraphModel graphModel) {
		graphFactory = graphModel.factory();
	}
	
	@Override
	public GraphFactory get() {
		return graphFactory;
	}

}
