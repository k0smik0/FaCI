package net.iubris.facri.grapher.analyzer.graphstream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor;
import net.iubris.facri.utils.Printer;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Kruskal;
import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.flow.FordFulkersonAlgorithm;
import org.graphstream.algorithm.measure.AbstractCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.ConnectivityMeasure.EdgeConnectivityMeasure;
import org.graphstream.algorithm.measure.DegreeCentrality;
import org.graphstream.algorithm.measure.DegreeMeasure;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphstreamAnalyzer {
	
	private final Graph graph;
	private final String graphName;
	private final String myUserId;
	private final File resultsDir;
	private final Node egoNode;

	private Node nodeWithMaximumDegreeExcludingEgo;
//	private int intsetCounter;
	private double[] clusteringCoefficients; // array index is graph node index; array value is clustering coefficient
	private int[] degreeDistribution;
	private ArrayList<Node> degreeMap;
	private double density;
	private double diameter;
	private ArrayList<Node> maximumClique;
//	private Collection<IntSet> cliques;
	private List<Node> giantComponent;
	private ConnectedComponents connectedComponentsWithoutEgo;
	private int edgeConnectivity;
	
	
	public GraphstreamAnalyzer(Graph graph, Node egoNode/*, @Named("my_user_id") String myUserId*/) {
		this.graph = graph;
		this.egoNode = egoNode;
		graphName = graph.getAttribute(GrapherExecutor.graph_name);
		
		myUserId = graphName.split("_-_")[0];
		
		resultsDir = new File("results"+File.separatorChar+myUserId);
		if (!resultsDir.exists())
			resultsDir.mkdirs();
		Printer.println("(results not printed here will be available in 'results' directory)");
	}
	
	/*
	 * Numerical zone
	 */	
	public void numericalAnalysis(boolean isDirectedGraph, String weightAttributeName) throws IOException {
		clusteringCoefficient();
		degree();
		density();
		diameter(weightAttributeName, isDirectedGraph);
		centralities();
		degreeMeasure();
	}
	
	public void clusteringCoefficient() throws IOException {
		Printer.print("Clustering coefficients: ");
		clusteringCoefficients = Toolkit.clusteringCoefficients(graph);
		BufferedWriter ccbw = getCSVBufferedWriter("clustering_coefficients");
		ccbw.write("NODE,Clustering Coefficient,UID\n");
		for (int i=0;i<clusteringCoefficients.length;i++)
			ccbw.write(i+","+clusteringCoefficients[i]+","+graph.getNode(i).getId()+"\n");
		ccbw.close();
		Printer.println("ok");
	}
	
	public void degree() throws IOException {
		Printer.print("Degree distribution: ");
		degreeDistribution = Toolkit.degreeDistribution(graph);
		BufferedWriter ddbw = getCSVBufferedWriter("degree_distribution");
		ddbw.write("DEGREE,FREQUENCY\n");		
		for (int i=0;i<degreeDistribution.length;i++) {
			ddbw.write(i+","+degreeDistribution[i]+"\n");
		}
		ddbw.close();
		Printer.println("ok");
		
		Printer.print("Nodes degree list: ");
		degreeMap = Toolkit.degreeMap(graph);
		BufferedWriter ndlbw = getCSVBufferedWriter("nodes_degree_list");
		ndlbw.write("UID,DEGREE\n");
		degreeMap.stream().forEach( n->{
//			Printer.println("\t"+n.getId()+": "+n.getDegree())
			try {
				ndlbw.write(n.getId()+","+n.getDegree()+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		ndlbw.close();
		Printer.println("ok");
		
		nodeWithMaximumDegreeExcludingEgo = degreeMap.get(1);
		Printer.println("Node with maximum degree (excluding ego) has degree: "+nodeWithMaximumDegreeExcludingEgo.getDegree());

	}
	
	public void density() {
		Printer.print("Density: ");
		density = Toolkit.density(graph);
		Printer.println(density);
	}
	
	public void diameter(String weightAttributeName, boolean directed) {
		Printer.print("Diameter: ");
		diameter = Toolkit.diameter(graph, weightAttributeName, directed);
		Printer.println(diameter);
	}
	
	public void centralities() throws IOException {
		
		centrality(new ClosenessCentrality(), DegreeCentrality.DEFAULT_ATTRIBUTE_KEY, "Closeness Centrality");
		
		centrality(new DegreeCentrality(), DegreeCentrality.DEFAULT_ATTRIBUTE_KEY, "Degree Centrality");
		
		centrality(new EigenvectorCentrality(), EigenvectorCentrality.DEFAULT_ATTRIBUTE_KEY, "Eigenvector Centrality");

		Printer.print("Betweenness Centrality: ");
		BetweennessCentrality betweennessCentrality = new BetweennessCentrality();
//		betweennessCentrality.registerProgressIndicator(new Progress() {
//			@Override
//			public void progress(float percent) {
//				Printer.print(percent+"% ");
//			}
//		});
		betweennessCentrality.init(graph);
		betweennessCentrality.compute();
		String label = "betweenness_centrality";
		BufferedWriter bbw = getCSVBufferedWriter(label);
		bbw.write("UID,"+"Betweenness Centrality".toUpperCase()+"\n");
		Iterator<Node> iterator = graph.iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			Double cb = node.getAttribute("Cb");
//			Printer.println(node.getId()+": "+cb);
			bbw.write(node.getId()+","+cb+"\n" );
		}
		bbw.close();
		Printer.println("ok");
	}
	protected void centrality(AbstractCentrality centrality, String attribute, String label) throws IOException {
		Printer.print(label+": ");
		centrality.init(graph);
		centrality.compute();
		centrality.copyValuesTo(attribute);
		Iterator<Node> iterator = graph.iterator();
		BufferedWriter cbw = getCSVBufferedWriter(label.replace(" ", "_").toLowerCase());
		cbw.write("UID,"+label.toUpperCase()+"\n");
		while (iterator.hasNext()) {
			Node node = iterator.next();
//			Printer.println(node.getId()+": "+ node.getAttribute(attribute) );
			cbw.write(node.getId()+","+node.getAttribute(attribute)+"\n" );			
		}
		cbw.close();
		Printer.println("ok");
	}
	
	public void degreeMeasure() {
		Printer.println("Degree Measure: ");
		DegreeMeasure degreeMeasure = new DegreeMeasure();
		degreeMeasure.init(graph);
		degreeMeasure.compute();
		Printer.println("\tx:"+degreeMeasure.getXMin()+","+degreeMeasure.getXMax());
		Printer.println("\ty:"+degreeMeasure.getYMin()+","+degreeMeasure.getYMax());
		Printer.println("mean: ("+degreeMeasure.getXMean()+","+degreeMeasure.getYMean()+")");
//		degreeMeasure.getXYSeries().getItems().stream().forEach(i->Printer.print(i+" "));
//		try {
//			degreeMeasure.plot();
//		} catch (PlotException e) {
//			e.printStackTrace();
//		}
		Printer.println("");
	}
	
	public void edgeConnectivity() {
//		Printer.print("Edge Connectivity: ");
		EdgeConnectivityMeasure edgeConnectivityMeasure = new EdgeConnectivityMeasure();
		edgeConnectivityMeasure.init(graph);
		edgeConnectivityMeasure.compute();
		edgeConnectivity = edgeConnectivityMeasure.getEdgeConnectivity();
		edgeConnectivityMeasure.terminate();
		Printer.println(edgeConnectivity);
//		return edgeConnectivity;
	}
	
	public void MaximumFlow() {
		FordFulkersonAlgorithm ffa = new FordFulkersonAlgorithm();
		ffa.init(graph);
		ffa.compute();
		ffa.getMaximumFlow();
	}
	
	public void MST() {
//		Prim p = new Prim();
		new Kruskal();
		new TarjanStronglyConnectedComponents();
	}
	
	public void StrongConnectedComponents() {
		TarjanStronglyConnectedComponents tarjanStronglyConnectedComponents = new TarjanStronglyConnectedComponents();
		tarjanStronglyConnectedComponents.setSCCIndexAttribute("ssc");
		tarjanStronglyConnectedComponents.init(graph);
		tarjanStronglyConnectedComponents.compute();
	}
	
//	public void epidemicCommunity() {
//		EpidemicCommunityAlgorithm epidemicCommunityAlgorithm = new EpidemicCommunityAlgorithm();
//		epidemicCommunityAlgorithm.init(graph);
//		epidemicCommunityAlgorithm.compute();
//		epidemicCommunityAlgorithm.
//	}

	
	/*
	 * Graphical zone
	 */
	public void graphicalAnalysis() throws IOException {
		cliques();
		/*try {
			cliquer();
		} catch (ParseException | SAXException e) {
			e.printStackTrace();
			return;
		}*/
		connected();
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
			String defaultUiClassAttribute = node.getAttribute("ui.class");
			node.addAttribute("ui.class", defaultUiClassAttribute+",maxclique");
		});
		Printer.println("");
		
		return maximumClique;
	}
	
	// using grph
	/*public Collection<IntSet> cliquer() throws ParseException, SAXException, IOException {
//System.out.println(graphName);
		String myUserId = graphName.split("_-_")[0];
		Path dirPath = FileSystems.getDefault().getPath("cache"+File.separator+"graphs"+File.separator+myUserId);
		Stream<Path> paths = Files.list(dirPath);
//paths.forEach(f->System.out.println(f));
		String graphFileBaseName =  graphName
//				.split("_-_")[1];
				.replace(myUserId+"_-_", "");
//System.out.println(graphFileBaseName);
		Optional<Path> file = paths.filter(f->f.toString().contains(graphFileBaseName)).findFirst();
//System.out.println( file.isPresent() );
		paths.close();
		if (file.isPresent()) {
			String fileName = file.get().toString();
//System.out.println(fileName);
			Grph fromGraphML = Grph.fromGraphML(fileName);
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
	}*/

	public void connected() {
		graph.removeNode(egoNode);
		
		connectedComponentsWithoutEgo = new ConnectedComponents(graph);
		connectedComponentsWithoutEgo.compute();
		Printer.println("Connected components without Ego: "+connectedComponentsWithoutEgo.getConnectedComponentsCount());
		
		giantComponent = connectedComponentsWithoutEgo.getGiantComponent();
		Printer.print("Giant component without Ego: "+giantComponent.size()+"\n\t");
		giantComponent.stream().parallel().forEach( n-> {
//			n.getAttributeKeySet().stream().forEach(a->System.out.print(a+" "));
			String defaultUiClassAttribute = n.getAttribute("ui.class");
			n.setAttribute("ui.class", defaultUiClassAttribute+",giantcomponent");
//			Printer.print(n.getId()+" ");
			});
		Printer.println("");
	}
	
	
	private BufferedWriter getCSVBufferedWriter(String suffix) throws IOException {
		BufferedWriter bw = Files.newBufferedWriter(FileSystems.getDefault().getPath(resultsDir.getPath()+File.separatorChar+graphName+"_-_"+suffix+".csv"));
		return bw;
	}
	
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo() {
		return nodeWithMaximumDegreeExcludingEgo;
	}
	
	/*public static void main(String[] args) {
		String graphName = "836460098_-_interactions_-_me_and_my_friends";
		String myUserId = graphName.split("_-_")[0];
		Path dirPath = FileSystems.getDefault().getPath("cache"+File.separator+"graphs"+File.separator+myUserId);
		Stream<Path> paths;
		try {
			paths = Files.list(dirPath);
			//paths.forEach(f->System.out.println(f));
			String graphFileBaseName =  graphName
//					.split("_-_")[1];
					.replace(myUserId+"_-_", "");
	System.out.println(graphFileBaseName);
			Optional<Path> file = paths.filter(f->f.toString().contains(graphFileBaseName)).findFirst();
	System.out.println( file.isPresent() );
			if (file.isPresent()) {
				String fileName = file.get().toString();
	//System.out.println(fileName);
				Grph fromGraphML = Grph.fromGraphML(fileName);
				Printer.print("Cliques (cliquer): ");
				AtomicInteger intsetCounter= new AtomicInteger(0);
				Collection<IntSet> cliques = fromGraphML.getCliques();
				Printer.println( ""+cliques.size() );
				cliques.stream().forEach( clique-> {
					Printer.println("\t"+(intsetCounter.incrementAndGet())+": ");
					Printer.println("\t\tgreatest: "+clique.getGreatest() );
					Printer.println("\t\tdensity: "+clique.getDensity() );
					clique.forEach(ic->{ Printer.println("\t\t\t"+ic.index+": "+ic.value); });
				});
			}
		} catch (IOException | ParseException | SAXException e) {
			e.printStackTrace();
		}
	}*/
	
	/*public static void main(String[] args) {
		double[] doubles =  new double[]{0.1, 0.2};
		try {
			CSVWriter csvWriter = new CSVWriter(new FileWriter( new File("/tmp/demo.csv")));
//			csvWriter.w
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
