package net.iubris.facri.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri.graph.exporter.GraphExporter;
import net.iubris.facri.graph.generator.FriendshipsGraphGenerator;
import net.iubris.facri.graph.generator.InteractionsGraphGenerator;
import net.iubris.facri.graph.generator.gephi.GephiFriendshipsGraphGenerator;
import net.iubris.facri.graph.generator.gephi.GephiInteractionsGraphGenerator;
import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.parsers.DataParser;
import net.iubris.facri.utils.Timing;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.BetweennessCentrality;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.GraphRenderer;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	void doParse() throws FileNotFoundException, JAXBException, XMLStreamException {
		dataParser.parse();
	}
	void createFriendnessGephiGraphs(boolean createGraphmlFile) {
		System.out.println("");
		
		System.out.print("generating *my friends* friendness graph:");
		friendshipsGraphGenerator.generateMyFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("friendess_graph_-_my_friends");
		System.out.println(" ok.");
		((GephiFriendshipsGraphGenerator)friendshipsGraphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *me with my friends* friendness graph: ");
		friendshipsGraphGenerator.clear();
		friendshipsGraphGenerator.generateMeWithMyFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends");
		System.out.println(" ok.");
		((GephiFriendshipsGraphGenerator)friendshipsGraphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *my friends with their friends* friendness graph: ");
		friendshipsGraphGenerator.clear();
		friendshipsGraphGenerator.generateMyFriendsAndFriendOfFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiFriendshipsGraphGenerator)friendshipsGraphGenerator).testGraph();
		System.out.println("");

		System.out.print("generating *me with my friends with their friends* friendness graph: ");
		friendshipsGraphGenerator.clear();
		friendshipsGraphGenerator.generateAll();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiFriendshipsGraphGenerator)friendshipsGraphGenerator).testGraph();
		System.out.println("");		
	}
	
	void createInteractionsGephiGraphs(boolean createGraphmlFile) {
		System.out.println("");
		
		System.out.print("generating *my friends* interactions graph:");
		interactionsGraphGenerator.generateMyFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("interactions_graph_-_my_friends");
		System.out.println(" ok.");
		((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *me with my friends graph*: ");
		interactionsGraphGenerator.clear();
		interactionsGraphGenerator.generateMeWithMyFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("interactions_graph_-_me_with_my_friends");
		System.out.println(" ok.");
		((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *my friends with their friends* graph: ");
		interactionsGraphGenerator.clear();
		interactionsGraphGenerator.generateMyFriendsAndFriendOfFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("interactions_graph_-_me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *me with my friends with their friends* interactions graph: ");
		interactionsGraphGenerator.clear();
		interactionsGraphGenerator.generateAll();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("interactions_graph_-me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
		System.out.println("");
	}
	
	void createInteractionsGraphstreamGraph() {
		System.out.print("\ngenerating *me with my friends graph*: ");
//		System.out.println("");
		Graph graph = graphstreamInteractionsGraphGenerator.getGraph();
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
////		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.enableAutoLayout();
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.display();
		
		graphstreamInteractionsGraphGenerator.generateMeWithMyFriends();
		System.out.println(" ok.");
		graphstreamInteractionsGraphGenerator.testGraph();
		

		
//		BetweennessCentrality betweennessCentrality = new BetweennessCentrality();
//		betweennessCentrality.betweennessCentrality(graph);
		
		
		
//		ViewerPipe fromViewer = viewer.newViewerPipe();
////      fromViewer.addViewerListener(this);
//      fromViewer.addSink(graph);
//      fromViewer.pump();
	}
	
	private final DataParser dataParser;
	private final FriendshipsGraphGenerator friendshipsGraphGenerator;
	private final GraphExporter graphExporter;
	
	private final CacheUtils cacheUtils;
	private final InteractionsGraphGenerator interactionsGraphGenerator;
	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	@Inject
	public Main(DataParser dataParser, 
			FriendshipsGraphGenerator friendshipsGraphGenerator, 
			InteractionsGraphGenerator interactionsGraphGenerator, 
			GraphExporter graphExporter, 
			CacheUtils cacheUtils,
			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		this.dataParser = dataParser;
		this.friendshipsGraphGenerator = friendshipsGraphGenerator;
		this.interactionsGraphGenerator = interactionsGraphGenerator;
		this.graphExporter = graphExporter;
		this.cacheUtils = cacheUtils;
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector( new FacriModule() );
		Main main = injector.getInstance(Main.class);
		
		try {
			if (args.length == 0) {
				main.printHelp();
			} else {
				String action = args[0];				
				if (args.length == 1) {
					main.doParse();
					if (action.equals("dummy")) {
//						main.createFriendnessGephiGraphs(false);
//						main.createInteractionsGephiGraphs(false);
						main.createInteractionsGraphstreamGraph();
					}
					if (action.equals("graphs")) {
						main.createFriendnessGephiGraphs(true);
						main.createInteractionsGephiGraphs(true);
					} 
				}			
				if (args.length == 2) {
					String cacheFilename = args[1];
					if (action.equals("read")) {
						Timing timing = new Timing();
						System.out.print("Reading "+cacheFilename+"...");
						main.cacheUtils.parseCache(cacheFilename);
						System.out.println(" ok ("+Math.ceil( timing.getTiming() )+"sec).");
					}
					if (action.equals("save")) {
						main.doParse();
						System.out.print("Writing "+cacheFilename+"...");
						main.cacheUtils.writeCache(cacheFilename);
						System.out.println(" ok.");
					}
					main.createFriendnessGephiGraphs(true);
					main.createInteractionsGephiGraphs(true);
				}
			}
//			System.exit(0);
		} catch (IOException | JAXBException | XMLStreamException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void printHelp() {
		System.out.println("Facri: [save | read] cacheFilename");
	}
}
