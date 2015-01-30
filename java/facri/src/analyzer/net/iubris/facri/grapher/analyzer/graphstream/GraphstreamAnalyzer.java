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

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.graph.utils.GraphCloner;
import net.iubris.facri.model.graph.utils.GraphCloner.GraphDataHolder;
import net.iubris.facri.utils.Printer;

import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.TarjanStronglyConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.community.EpidemicCommunityAlgorithm;
import org.graphstream.algorithm.flow.FordFulkersonAlgorithm;
import org.graphstream.algorithm.measure.AbstractCentrality;
import org.graphstream.algorithm.measure.ClosenessCentrality;
import org.graphstream.algorithm.measure.ConnectivityMeasure.EdgeConnectivityMeasure;
import org.graphstream.algorithm.measure.DegreeCentrality;
import org.graphstream.algorithm.measure.DegreeMeasure;
import org.graphstream.algorithm.measure.EigenvectorCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;


public class GraphstreamAnalyzer {
	
	private Graph graph;
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
	
	private final GraphCloner graphCloner;
	private GraphDataHolder graphCopyDataHolder;	
	
	@AssistedInject
	public GraphstreamAnalyzer(@Assisted Graph graph, @Assisted Node egoNode, GraphCloner graphCloner) {
				graphCopyDataHolder = graphCloner.copyWithMouseManager(graph, graph.getId()+" - to analyze");
				this.graph = 
//				graph;
						graphCopyDataHolder.getGraph();
				int removed = NoZeroDegree.removeZeroDegreeNodes(this.graph);
				Printer.println("Dichotomized "+this.graph.getId()+"removing node with degree=0: removed "+removed+" nodes.");
		this.egoNode = egoNode;
		this.graphCloner = graphCloner;
		this.graphName = graph.getAttribute(GraphsHolder.graph_file_name);
		
		this.myUserId = graphName.split("_-_")[0];
		
		this.resultsDir = new File("results"+File.separatorChar+myUserId);
		if (!resultsDir.exists())
			resultsDir.mkdirs();
		Printer.println("(results not printed here will be available in 'results' directory)");	
	}
	
	/*
	 * Numerical zone
	 */	
	public void numericalAnalysis(String weightAttributeName, boolean isDirectedGraph) throws IOException {
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
	
//	public void MST() {
////		Prim p = new Prim();
//		new Kruskal();
//		new TarjanStronglyConnectedComponents();
//	}
	
	/*
	 * Graphical zone
	 */
	public void graphicalAnalysis() {
		if (degreeMap == null)
			degreeMap = Toolkit.degreeMap(graph);
		
		cliques();
//		strongConnectedComponents();
//		dijkstra();
//		epidemicCommunity();
		
		// always as last - bugged? !
		connectedComponents();
	}
	
	public ArrayList<Node> cliques() {
//		Printer.print("Maximal Cliques: ");
		GraphDataHolder graphDataHolder = graphCloner.copyWithMouseManager(graph,graph.getId()+" - cliques");
		Graph graph2 = graphDataHolder.getGraph();

		Iterable<List<Node>> maximalCliques = Toolkit.getMaximalCliques(graph2);
		
//		AtomicInteger maximalCliquesCounter = new AtomicInteger(0);
//		maximalCliques.iterator().forEachRemaining(nodesList->{
//			maximalCliquesCounter.incrementAndGet();
//			nodesList.forEach(node->node.setAttribute("clique"));
//		});
		
		maximumClique = new ArrayList<Node>(0);
		//naive
		/*for (List<Node> clique : maximalCliques)
			if (clique.size() > tempMaximumClique.size())
				tempMaximumClique = clique;*/
		
//		Printer.println("Maximumal Cliques: "+maximalCliquesCounter.get());
//		int removed = ClearUselessNodesAction.removeZeroDegreeNodes(graph2);
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
//		Printer.println("(Dichotomized with degree>0: removed "+removed+" nodes.)\n");
		
		
		return maximumClique;
	}
	
	public void connectedComponents() {
//		GraphDataHolder graphDataHolderBackup = 
//				graphCloner.clone(graph);
		
		GraphDataHolder graphDataHolder2 = graphCloner.copyWithMouseManager(graph, graph.getId()+" - giant connected components");
		Graph graph2 = graphDataHolder2.getGraph();
		graph2.removeNode(egoNode);
		
//		int removed = ClearUselessNodesAction.removeZeroDegreeNodes(graph2);
		connectedComponentsWithoutEgo = new ConnectedComponents();
		connectedComponentsWithoutEgo.init(graph2);
		try {
			connectedComponentsWithoutEgo.compute();
		} catch (NullPointerException npe) {}
		Printer.println("Connected components without Ego: "+connectedComponentsWithoutEgo.getConnectedComponentsCount());
		
		giantComponent = connectedComponentsWithoutEgo.getGiantComponent();
		Printer.print("Giant component without Ego: "+giantComponent.size()+"\n\t");
		giantComponent.stream().parallel().forEach( n-> {
//			n.getAttributeKeySet().stream().forEach(a->System.out.print(a+" "));
			String defaultUiClassAttribute = n.getAttribute("ui.class");
			n.setAttribute("ui.class", defaultUiClassAttribute+",giantcomponent");
//			Printer.print(n.getId()+" ");
			});
//		Printer.println("Dichotomized with degree>0: removed "+removed+" nodes.");

//		graphDataHolderBackup.getGraph().setAttribute("ui.title", graph.getId()+" - giant connected components");
		
//		GraphDataHolder graphDataHolder = 
//		graphCloner.copyWithMouseManager(graph, graph.getId()+" - giant connected components");
//		Graph graph2 = graphDataHolder.getGraph();

//		graph.clear();
//		Graph b = graphDataHolderBackup.getGraph();
//		graph = GraphCloner.copy(b);
		graph.setAttribute("ui.title", "close me");
		graph.display().close();
//		b = null;
//		Pauser.sleep(5000);
//		graphDataHolderBackup.getViewer().close();
		
//		graphDataHolderBackup.getViewer().close();
//		Graphs.merge(graph, graphDataHolderBackup.getGraph());
//		graph.setAttribute("ui.title", graphDataHolderBackup.getGraph().getId());
		
		Printer.println("");
	}
	
	public void strongConnectedComponents() {
		GraphDataHolder graphDataHolder = graphCloner.copyWithMouseManager(graph, graph.getId()+" - strong connected components");
		Graph graph2 = graphDataHolder.getGraph();
		graph2.removeNode(egoNode);
		NoZeroDegree.removeZeroDegreeNodes(graph2);
		TarjanStronglyConnectedComponents tarjanStronglyConnectedComponents = new TarjanStronglyConnectedComponents();
		tarjanStronglyConnectedComponents.setSCCIndexAttribute("ssc");
		tarjanStronglyConnectedComponents.init(graph2);
		tarjanStronglyConnectedComponents.compute();
		Printer.println("");
	}
	
	public void dijkstra() {
		GraphDataHolder graphDataHolder = graphCloner.copyWithMouseManager(graph, graph.getId()+" - dijkstra");
		Graph graph2 = graphDataHolder.getGraph();
		graph2.removeNode(egoNode);
		Dijkstra dijkstra = new Dijkstra(null,"dijkstra","weight");
		dijkstra.init(graph2);
		NoZeroDegree.removeZeroDegreeNodes(graph2);
		dijkstra.setSource( degreeMap.get(degreeMap.size()-1) );
		dijkstra.compute();
		Node source = dijkstra.getSource();
		double treeLength = dijkstra.getTreeLength();
		Printer.println("Dijkstra: from "+source.getId()+" count "+treeLength+"\n");
	}
	
	public void epidemicCommunity() {
		GraphDataHolder graphDataHolder = graphCloner.copyWithMouseManager(graph, graph.getId()+" - epidemic community");
		Graph graph2 = graphDataHolder.getGraph();
		NoZeroDegree.removeZeroDegreeNodes(graph2);
		EpidemicCommunityAlgorithm epidemicCommunityAlgorithm = new EpidemicCommunityAlgorithm();
		epidemicCommunityAlgorithm.init(graph2);
//		epidemicCommunityAlgorithm.setMarker("epidemic");
		epidemicCommunityAlgorithm.compute();
//		graph2.getNodeSet().stream().forEach(n->n.getAttributeKeySet().stream().forEach( a->System.out.println(a+" ") ));
//		System.out.println(
//			epidemicCommunityAlgorithm.getMarker()
//		);
		Printer.println("");
	}
	
	private BufferedWriter getCSVBufferedWriter(String suffix) throws IOException {
		BufferedWriter bw = Files.newBufferedWriter(FileSystems.getDefault().getPath(resultsDir.getPath()+File.separatorChar+graphName+"_-_"+suffix+".csv"));
		return bw;
	}
	
	public Node getFirstNodeWithMaximumDegreeExceptEgo() {
		return nodeWithMaximumDegreeExcludingEgo;
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
	
	public static interface AnalysisTypeShortcut {
		public String getHelpMessage();
		public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException;
		
		public enum Numerical implements AnalysisTypeShortcut {
			cc {
				@Override
				public String getHelpMessage() {
					return "clustering coefficients";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.clusteringCoefficient();
				}
			},
			dg {
				@Override
				public String getHelpMessage() {
					return "degree";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.degree();
				}
			},
			ds {
				@Override
				public String getHelpMessage() {
					return "density";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.density();
				}
			},
			di {
				@Override
				public String getHelpMessage() {
					return "diameter";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					String weightAttributeName = (String) params[0];
					boolean isDirectedGraph = (boolean) params[1];
					graphstreamAnalyzer.diameter(weightAttributeName, isDirectedGraph);
				}
			},
			c {
				@Override
				public String getHelpMessage() {
					return "centrality";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.centralities();
				}
			},
			e {
				@Override
				public String getHelpMessage() {
					return "edge connectivity";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.edgeConnectivity();
				}
			},
			a {
				@Override
				public String getHelpMessage() {
					return "all numerical measures";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					String weightAttributeName = (String) params[0];
					boolean isDirectedGraph = (boolean) params[1];
					graphstreamAnalyzer.numericalAnalysis(weightAttributeName, isDirectedGraph);
				}
			};
			public abstract String getHelpMessage();
		}
		public enum Graphical implements AnalysisTypeShortcut {
			cl {
				@Override
				public String getHelpMessage() {
					return "cliques";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.cliques();
				}
			},
			cc {
				@Override
				public String getHelpMessage() {
					return "connected components";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.connectedComponents();
				}
			},
			scc {
				@Override
				public String getHelpMessage() {
					return "strong connected components";
				}
				@Override
				public void doAnalysis(GraphstreamAnalyzer graphstreamAnalyzer, Object... params) throws IOException {
					graphstreamAnalyzer.strongConnectedComponents();
				}
			};
			public abstract String getHelpMessage();
		}
	}
	
	
	public interface GraphstreamAnalyzerFactory {
		GraphstreamAnalyzer create(Graph graph, Node node);
	}
}
