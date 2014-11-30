package net.iubris.facri.graph.analyzer.graphstream;

import java.util.List;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphstreamInteractionsAnalyzer {
	
	private final Graph graph;
	private Node egoNode;
	
	public GraphstreamInteractionsAnalyzer(Graph graph, Node node) {
		this.graph = graph;
		this.egoNode = node;
	}

	public void analyzeConnected() {
		graph.removeNode(egoNode);
		ConnectedComponents connectedComponents = new ConnectedComponents(graph);
		List<Node> giantComponent = connectedComponents.getGiantComponent();
		System.out.println("giant: "+giantComponent.size());
		
		giantComponent.stream().parallel().forEach(n->n.setAttribute("ui.class", "giantcomponent"));		
	}
}
