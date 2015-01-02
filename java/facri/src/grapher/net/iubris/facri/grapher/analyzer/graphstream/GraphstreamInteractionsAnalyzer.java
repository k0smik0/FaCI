package net.iubris.facri.grapher.analyzer.graphstream;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphstreamInteractionsAnalyzer {
	
	private final Graph graph;
	private final Node egoNode;
	
//	@Inj
	public GraphstreamInteractionsAnalyzer(Graph graph, Node egoNode) {
		this.graph = graph;
		this.egoNode = egoNode;
	}
	
	public void degree() {
		int[] degreeDistribution = Toolkit.degreeDistribution(graph);
		ArrayList<Node> degreeMap = Toolkit.degreeMap(graph);
	}
	
	public void density() {
		double density = Toolkit.density(graph);
	}
	
	public void diameter(String weightAttributeName, boolean directed) {
		double diameter = Toolkit.diameter(graph, weightAttributeName, directed);
	}
	
	public void clusteringCoefficient() {
		double[] clusteringCoefficients = Toolkit.clusteringCoefficients(graph);
	}
	
	public void cliques() {
		Iterable<List<Node>> maximalCliques = Toolkit.getMaximalCliques(graph);
		List<Node> maximumClique = new ArrayList<Node>();
		//naive
		for (List<Node> clique : maximalCliques)
			if (clique.size() > maximumClique.size())
				maximumClique = clique;
	}

	public void analyzeConnected() {
		// TODO this is just a demo
		graph.removeNode(egoNode);
		ConnectedComponents connectedComponents = new ConnectedComponents(graph);
		List<Node> giantComponent = connectedComponents.getGiantComponent();
		System.out.println("giant: "+giantComponent.size());
		
		giantComponent.stream().parallel().forEach(n->n.setAttribute("ui.class", "giantcomponent"));		
	}
}
