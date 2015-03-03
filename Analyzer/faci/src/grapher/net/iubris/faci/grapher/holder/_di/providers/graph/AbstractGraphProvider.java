package net.iubris.faci.grapher.holder._di.providers.graph;

import javax.inject.Provider;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

public abstract class AbstractGraphProvider implements Provider<Graph> {

	private final Graph graph;

	public AbstractGraphProvider(String graphName) {
		graph = new MultiGraph(graphName,false,true);
	}

	@Override
	public Graph get() {
		return graph;
	}

}
