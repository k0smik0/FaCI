package net.iubris.facri.grapher.generators.graphstream;

import net.iubris.facri.grapher.generators.GraphGenerator;

import org.graphstream.graph.Graph;

public interface GraphstreamGraphGenerator extends GraphGenerator {
	Graph getGraph();
	void setGraphAsGenerated();
}
