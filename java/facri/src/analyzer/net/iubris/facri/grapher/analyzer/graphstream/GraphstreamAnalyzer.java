package net.iubris.facri.grapher.analyzer.graphstream;

import grph.Grph;
import grph.io.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import toools.set.IntSet;

public class GraphstreamAnalyzer {
	
	private final Graph graph;
	private final Node egoNode;
	private Node firstNodeWithMaximumDegreeExceptEgo;
	private int intsetCounter;
	
//	@Inj
	public GraphstreamAnalyzer(Graph graph, Node egoNode) {
		this.graph = graph;
		this.egoNode = egoNode;
	}
	
	public void clusteringCoefficient() {
		System.out.print("Clustering coefficients:\n\t");
		double[] clusteringCoefficients = Toolkit.clusteringCoefficients(graph);
		for (double cc: clusteringCoefficients) {
			System.out.print(cc+" ");
		}
		System.out.println("");
	}
	
	public void degree() {
		System.out.print("Degree distribution:\n\t");
		int[] degreeDistribution = Toolkit.degreeDistribution(graph);
		for (int d: degreeDistribution) {
			System.out.print(d+" ");
		}
//		StreamSupport.intStream(Spliterators.spliterator(degreeDistribution, 0, degreeDistribution.length, Spliterator.SIZED), false).forEach(d->System.out.print(d+" "));
//				degreeDistribution, false);
		System.out.println("");
		
		System.out.println("Nodes degree list:");
		ArrayList<Node> degreeMap = Toolkit.degreeMap(graph);
		degreeMap.stream().forEach( n->System.out.println("\t"+n.getId()+": "+n.getDegree()) );
		firstNodeWithMaximumDegreeExceptEgo = degreeMap.get(1);
		
		System.out.println("");
	}
	
	public void density() {
		System.out.print("Density: ");
		double density = Toolkit.density(graph);
		System.out.println(density+"\n");
	}
	
	public void diameter(String weightAttributeName, boolean directed) {
		System.out.print("Diameter: ");
		double diameter = Toolkit.diameter(graph, weightAttributeName, directed);
		System.out.println(diameter+"\n");
	}
	
	public void cliques() {
		System.out.print("Maximal Cliques: ");
		Iterable<List<Node>> maximalCliques = Toolkit.getMaximalCliques(graph);
		
		final List<Node> maximumClique = new ArrayList<Node>(0);
		//naive
		/*for (List<Node> clique : maximalCliques)
			if (clique.size() > tempMaximumClique.size())
				tempMaximumClique = clique;*/
		
		System.out.print("Maximum Clique: ");
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
		System.out.print(maximumClique.size()+" nodes\n\t");
		maximumClique.forEach( new Consumer<Node>() {
			@Override
			public void accept(Node t) {
				System.out.print(t.getId()+" ");
				t.addAttribute("ui.class", "maxclique");
			}
		});
		System.out.println("");
	}
	
	public void cliquer() throws ParseException, SAXException {
		Grph fromGraphML = Grph.fromGraphML("graphmls/836460098_-_interactions_graph_-_me_with_friends.graphml");
		System.out.print("Cliques (cliquer): ");
		intsetCounter=0;
		Collection<IntSet> cliques = fromGraphML.getCliques();
		System.out.println( cliques.size() );
		cliques.stream().forEach( is-> {
			System.out.println("\t"+(++intsetCounter)+": ");
			System.out.println("\t\tgreatest: "+is.getGreatest() );
			System.out.println("\t\tdensity: "+is.getDensity() );
			is.forEach(ic->{ System.out.println("\t\t\t"+ic.index+": "+ic.value); });
		});
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
		try {
			cliquer();
		} catch (ParseException | SAXException e) {
			e.printStackTrace();
			return;
		}
		connected();
	}
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo() {
		return firstNodeWithMaximumDegreeExceptEgo;
	}

}
