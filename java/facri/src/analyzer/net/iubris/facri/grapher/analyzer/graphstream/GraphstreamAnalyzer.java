package net.iubris.facri.grapher.analyzer.graphstream;

import grph.Grph;
import grph.io.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor;
import net.iubris.facri.utils.Printer;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.GraphFactory;
import org.graphstream.graph.Node;
import org.xml.sax.SAXException;

import toools.set.IntSet;

public class GraphstreamAnalyzer {
	
	private final Graph graph;
	private final String graphName;
	private final Node egoNode;

	private Node nodeWithMaximumDegreeExcludingEgo;
	private int intsetCounter;
	private double[] clusteringCoefficients;
	private int[] degreeDistribution;
	private ArrayList<Node> degreeMap;
	private double density;
	private double diameter;
	private ArrayList<Node> maximumClique;
	private Collection<IntSet> cliques;
	private List<Node> giantComponent;
	private ConnectedComponents connectedComponentsWithoutEgo;
	
	
	
	public GraphstreamAnalyzer(Graph graph, Node egoNode/*, @Named("my_user_id") String myUserId*/) {
		this.graph = graph;
		this.egoNode = egoNode;
		graphName = graph.getAttribute(GrapherExecutor.graph_name);
		
		File resultsDir = new File("results");
		if (!resultsDir.exists())
			resultsDir.mkdirs();
	}
	
	public double[] clusteringCoefficient() {
		Printer.print("Clustering coefficients:\n\t");
		clusteringCoefficients = Toolkit.clusteringCoefficients(graph);
		for (double cc: clusteringCoefficients) {
			Printer.print(cc+" ");
		}
		Printer.println("");
		return clusteringCoefficients;
	}
	
	public int[] degree() {
		Printer.print("Degree distribution:\n\t");
		degreeDistribution = Toolkit.degreeDistribution(graph);
		for (int d: degreeDistribution) {
			Printer.print(d+" ");
		}
//		StreamSupport.intStream(Spliterators.spliterator(degreeDistribution, 0, degreeDistribution.length, Spliterator.SIZED), false).forEach(d->Printer.print(d+" "));
//				degreeDistribution, false);
		Printer.println("");
		
		Printer.println("Nodes degree list:");
		degreeMap = Toolkit.degreeMap(graph);
		degreeMap.stream().forEach( n->Printer.println("\t"+n.getId()+": "+n.getDegree()) );
		nodeWithMaximumDegreeExcludingEgo = degreeMap.get(1);
		
		Printer.println("");
		return degreeDistribution;
	}
	
	public double density() {
		Printer.print("Density: ");
		density = Toolkit.density(graph);
		Printer.println(density+"\n");
		return density;
	}
	
	public double diameter(String weightAttributeName, boolean directed) {
		Printer.print("Diameter: ");
		diameter = Toolkit.diameter(graph, weightAttributeName, directed);
		Printer.println(diameter+"\n");
		
		return diameter;
	}
	
	public ArrayList<Node> cliques() {
//		Printer.print("Maximal Cliques: ");
		Iterable<List<Node>> maximalCliques = Toolkit.getMaximalCliques(graph);
		
		maximumClique = new ArrayList<Node>(0);
		//naive
		/*for (List<Node> clique : maximalCliques)
			if (clique.size() > tempMaximumClique.size())
				tempMaximumClique = clique;*/
		
		Printer.print("Maximum Clique: ");
		Spliterator<List<Node>> maximalCliquesSpliterator = maximalCliques.spliterator();
		StreamSupport.stream(maximalCliquesSpliterator, true).parallel()
		.forEach(nodes -> {
			if (nodes.size() > maximumClique.size()) {
				maximumClique.clear();
				maximumClique.addAll(nodes);
			}
		});
		Printer.print(maximumClique.size()+" nodes\n\t");
		maximumClique.forEach( node -> {
			Printer.print(node.getId()+" ");
			node.addAttribute("ui.class", "maxclique");
		});
		Printer.println("");
		
		return maximumClique;
	}
	
	// using grph
	public Collection<IntSet> cliquer() throws ParseException, SAXException, IOException {
		String myUserId = graphName.split("_-_")[0];
		Path dirPath = FileSystems.getDefault().getPath("cache"+File.separator+"graphs"+File.separator+myUserId);
		Stream<Path> paths = Files.list(dirPath);
		Optional<Path> file = paths.filter(f->f.startsWith(graphName)).findFirst();
		paths.close();
		if (file.isPresent()) {			
			Grph fromGraphML = Grph.fromGraphML(file.get().toString());
			Printer.print("Cliques (cliquer): ");
			intsetCounter=0;
			cliques = fromGraphML.getCliques();
			Printer.println( ""+cliques.size() );
			cliques.stream().forEach( clique-> {
				Printer.println("\t"+(++intsetCounter)+": ");
				Printer.println("\t\tgreatest: "+clique.getGreatest() );
				Printer.println("\t\tdensity: "+clique.getDensity() );
				clique.forEach(ic->{ Printer.println("\t\t\t"+ic.index+": "+ic.value); });
			});
		}
		return cliques;
	}

	public void connected() {
		graph.removeNode(egoNode);
		connectedComponentsWithoutEgo = new ConnectedComponents(graph);
		connectedComponentsWithoutEgo.compute();
		Printer.println("Connected components without Ego: "+connectedComponentsWithoutEgo.getConnectedComponentsCount());

		giantComponent = connectedComponentsWithoutEgo.getGiantComponent();
		Printer.print("Giant component without Ego: "+giantComponent.size()+"\n\t");
		giantComponent.stream().parallel().forEach( n-> { 
			n.setAttribute("ui.class", "giantcomponent");
			Printer.print(n.getId()+" ");
			});
		Printer.println("");
//		connected(a);
	}
	
	public void measures() {
		ClosenessCentrality closenessCentrality = new ClosenessCentrality();
		closenessCentrality.init(graph);
		closenessCentrality.compute();
		String closenessCentralityAttribute = closenessCentrality.getCentralityAttribute();
		
		EigenvectorCentrality eigenvectorCentrality = new EigenvectorCentrality();
		eigenvectorCentrality.init(graph);
		eigenvectorCentrality.compute();
		eigenvectorCentrality.compute();
		String eigenvectorCentralityAttribute = eigenvectorCentrality.getCentralityAttribute();
	}
	
	public void numericalAnalysis(boolean isDirectedGraph, String weightAttributeName) {
		clusteringCoefficient();
		degree();
		density();
		diameter(weightAttributeName, isDirectedGraph);
	}
	
	public void graphicalAnalysis() throws IOException {
		cliques();
		try {
			cliquer();
		} catch (ParseException | SAXException e) {
			e.printStackTrace();
			return;
		}
		connected();
	}
	
	public void doCharts() {
		
	}
	
	public void writeResults() throws IOException {
//		
		BufferedWriter ccbw = Files.newBufferedWriter(FileSystems.getDefault().getPath(graphName+"_-_clustering_coefficients"));
		for (double cc: clusteringCoefficients)
			ccbw.write(cc+" ");
		ccbw.close();
		
		BufferedWriter ddbw = Files.newBufferedWriter(FileSystems.getDefault().getPath(graphName+"_-_degree_distribution"));
		for (int dd: degreeDistribution)
			ccbw.write(dd+" ");
		ddbw.close();
		
		Printer.print("Node with maximum degree (excluding my user): "+nodeWithMaximumDegreeExcludingEgo.getId()+", with in-degree: "+nodeWithMaximumDegreeExcludingEgo.getInDegree()+", and out-degree: "+nodeWithMaximumDegreeExcludingEgo.getOutDegree());
		BufferedWriter dumbw = Files.newBufferedWriter(FileSystems.getDefault().getPath(graphName+"_-_degree_users_map"));
		Iterator<Node> iterator = degreeMap.iterator();
		while (iterator.hasNext()) {
			Node next = iterator.next();
			dumbw.write(next.getId()+" "+next.getDegree());
		}
		dumbw.close();
		
		Printer.println("diameter: "+diameter);
		Printer.println("density: "+density);
		
		
		
	}
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo() {
		return nodeWithMaximumDegreeExcludingEgo;
	}

}
