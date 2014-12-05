package net.iubris.facri.graph.generator.graphstream;

import net.iubris.facri.graph.generator.GraphGenerator;

import org.graphstream.graph.Graph;

public interface GraphstreamGraphGenerator extends GraphGenerator {
	Graph getGraph();
}
