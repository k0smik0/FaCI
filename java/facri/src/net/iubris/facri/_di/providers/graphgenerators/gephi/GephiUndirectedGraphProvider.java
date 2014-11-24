package net.iubris.facri._di.providers.graphgenerators.gephi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;

@Singleton
public class GephiUndirectedGraphProvider implements Provider<UndirectedGraph> {

	private final UndirectedGraph undirectedGraph;
	
	@Inject
	public GephiUndirectedGraphProvider(GraphModel graphModel) {
		this.undirectedGraph = graphModel.getUndirectedGraph();
	}
	
	@Override
	public UndirectedGraph get() {
		return undirectedGraph;
	}

}
