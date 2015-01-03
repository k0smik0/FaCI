package net.iubris.facri.grapher.generators;

import org.graphstream.graph.Graph;

public interface GraphstreamGraphGenerator extends GraphGenerator {
	Graph getGraph();
	void doneGraphGeneration();
}
