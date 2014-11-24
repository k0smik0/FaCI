package net.iubris.facri.main;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri.graph.exporter.GraphExporter;
import net.iubris.facri.graph.generator.GraphGenerator;
import net.iubris.facri.graph.generator.gephi.GephiFriendnessGraphGenerator;
import net.iubris.facri.parsers.DataParser;
import net.iubris.facri.utils.Timing;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	void doParse() throws FileNotFoundException, JAXBException, XMLStreamException {
		dataParser.parse();
	}
	void createFriendnessGraphs() {
		System.out.println("");
		
		System.out.print("generating *my friends* graph:");
		graphGenerator.generateMyFriends();
		graphExporter.exportGraphToGraphML("friendess_graph_-_my_friends");
		System.out.println(" ok.");
		((GephiFriendnessGraphGenerator)graphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *me with my friends graph*: ");
		graphGenerator.clear();
		graphGenerator.generateMeWithMyFriends();
		graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_friends");
		System.out.println(" ok.");
		((GephiFriendnessGraphGenerator)graphGenerator).testGraph();
		System.out.println("");
		
		System.out.print("generating *my friends with their friends* graph: ");
		graphGenerator.clear();
		graphGenerator.generateMyFriendsAndFriendOfFriends();
		graphExporter.exportGraphToGraphML("friendess_graph_-_me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiFriendnessGraphGenerator)graphGenerator).testGraph();
		System.out.println("");

		System.out.print("generating *me with my friends with their friends* graph: ");
		graphGenerator.clear();
		graphGenerator.generateAll();
		graphExporter.exportGraphToGraphML("friendess_graph_-_my_friends_with_me_and_their_friends");
		System.out.println(" ok.");
		((GephiFriendnessGraphGenerator)graphGenerator).testGraph();
		System.out.println("");		
	}
	
	private final DataParser dataParser;
	private final GraphGenerator graphGenerator;
	private final GraphExporter graphExporter;
//	private final World world;
	
	private final CacheUtils cacheUtils;
	
	@Inject
	public Main(DataParser dataParser, GraphGenerator graphGenerator, GraphExporter graphExporter, /*World world,*/ CacheUtils cacheUtils) {
		this.dataParser = dataParser;
		this.graphGenerator = graphGenerator;
		this.graphExporter = graphExporter;
//		this.world = world;
		this.cacheUtils = cacheUtils;
	}
	
	public static void main(String[] args) {
		Injector injector = Guice.createInjector( new FacriModule() );
		Main main = injector.getInstance(Main.class);
		
		try {
			if (args.length == 0) {
				main.printHelp();
			}
			if (args.length == 2) {
				String action = args[0];
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
				main.createFriendnessGraphs();
			} else if (args.length == 1) {
				main.doParse();
				main.createFriendnessGraphs();
//				injector.getInstance(World.class).testData();
			} 
			System.exit(0);
		} catch (IOException | JAXBException | XMLStreamException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void printHelp() {
		System.out.println("Facri: [save | read] cacheFilename");
	}
}
