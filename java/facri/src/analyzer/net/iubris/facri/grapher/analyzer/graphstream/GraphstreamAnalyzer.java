package net.iubris.facri.grapher.analyzer.graphstream;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphstreamAnalyzer {
	
	private final Graph graph;
	private final Node egoNode;
	private Node firstNodeWithMaximumDegreeExceptEgo;
	
//	@Inj
	public GraphstreamAnalyzer(Graph graph, Node egoNode) {
		this.graph = graph;
		this.egoNode = egoNode;
	}
	
	public void clusteringCoefficient() {
		double[] clusteringCoefficients = Toolkit.clusteringCoefficients(graph);
		System.out.print("Clustering coefficients:\n\t");
		for (double cc: clusteringCoefficients) {
			System.out.print(cc+" ");
		}
		System.out.println("");
	}
	
	public void degree() {
		int[] degreeDistribution = Toolkit.degreeDistribution(graph);
		System.out.print("Degree distribution:\n\t");
		for (int d: degreeDistribution) {
			System.out.print(d+" ");
		}
//		StreamSupport.intStream(Spliterators.spliterator(degreeDistribution, 0, degreeDistribution.length, Spliterator.SIZED), false).forEach(d->System.out.print(d+" "));
//				degreeDistribution, false);
		System.out.println("");
		
		ArrayList<Node> degreeMap = Toolkit.degreeMap(graph);
		System.out.println("Nodes degree list:");
		degreeMap.stream().forEach( n->System.out.println("\t"+n.getId()+": "+n.getDegree()) );
		firstNodeWithMaximumDegreeExceptEgo = degreeMap.get(1);
		
		System.out.println("");
	}
	
	public void density() {
		double density = Toolkit.density(graph);
		System.out.println("Density: "+density+"\n");
	}
	
	public void diameter(String weightAttributeName, boolean directed) {
		double diameter = Toolkit.diameter(graph, weightAttributeName, directed);
		System.out.println("Diameter: "+diameter+"\n");
	}
	
	public void cliques() {
		Iterable<List<Node>> maximalCliques = Toolkit.getMaximalCliques(graph);
		final List<Node> maximumClique = new ArrayList<Node>(0);
		
		//naive
		/*for (List<Node> clique : maximalCliques)
			if (clique.size() > tempMaximumClique.size())
				tempMaximumClique = clique;*/
		
		Spliterator<List<Node>> maximalCliquesSpliterator = maximalCliques.spliterator();
		StreamSupport.stream(maximalCliquesSpliterator, true).parallel()
		.forEach( new Consumer<List<Node>>() {
			@Override
			public void accept(List<Node> t) {
				if (t.size() > maximumClique.size()) {
					maximumClique.clear();
					maximumClique.addAll(t);
				}
			}
		});
		System.out.print("Maximum Clique: "+maximumClique.size()+" nodes\n\t");
		maximumClique.forEach( new Consumer<Node>() {
			@Override
			public void accept(Node t) {
				System.out.print(t.getId()+" ");
				t.addAttribute("ui.class", "maxclique");
			}
		});
		System.out.println("");
	}

	public void connected() {
		graph.removeNode(egoNode);
		ConnectedComponents connectedComponents = new ConnectedComponents(graph);
		connectedComponents.compute();
		System.out.println("Connected components without Ego: "+connectedComponents.getConnectedComponentsCount());

		List<Node> giantComponent = connectedComponents.getGiantComponent();
		System.out.print("Giant component without Ego: "+giantComponent.size()+"\n\t");
		giantComponent.stream().parallel().forEach( n-> { 
			n.setAttribute("ui.class", "giantcomponent");
			System.out.print(n.getId()+" ");
			});
		System.out.println("");
		
//		Printer a = s->System.out.println( s );
//		connected(a);
	}
	
	public void numericalAnalysis(boolean isDirectedGraph, String weightAttributeName) {
		clusteringCoefficient();
		degree();
		density();
		diameter(weightAttributeName, isDirectedGraph);
	}
	
	public void graphicalAnalysis() {
		cliques();
		connected();
	}
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo() {
		return firstNodeWithMaximumDegreeExceptEgo;
	}

}
