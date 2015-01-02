package net.iubris.facri._di.providers.grapher.graphgenerators.gephi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphModel;

@Singleton
public class GephiDirectedGraphProvider implements Provider<DirectedGraph> {

	private final DirectedGraph directedGraph;
	
	@Inject
	public GephiDirectedGraphProvider(GraphModel graphModel) {
		this.directedGraph = graphModel.getDirectedGraph();
	}
	
	@Override
	public DirectedGraph get() {
		return directedGraph;
	}

}
