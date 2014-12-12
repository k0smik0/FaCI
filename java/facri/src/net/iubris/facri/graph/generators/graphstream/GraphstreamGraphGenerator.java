package net.iubris.facri.graph.generators.graphstream;

import net.iubris.facri.graph.generators.GraphGenerator;

import org.graphstream.graph.Graph;

public interface GraphstreamGraphGenerator extends GraphGenerator {
	Graph getGraph();
}
