package net.iubris.facri.main;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import com.teradata.jdbc.jdbc_4.parcel.OptionsParcel;

import net.iubris.facri.main.options.Options;
import net.iubris.facri.main.options.OptionAction;

public class Main {
	

//	void doParse() throws FileNotFoundException, JAXBException, XMLStreamException {
//		dataParser.parse();
//	}
	
	
	/*void createInteractionsGephiGraphs(boolean createGraphmlFile) {
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
		interactionsGraphGenerator.generateMeWithMyFriendsAndTheirFriends();
		if (createGraphmlFile)
			graphExporter.exportGraphToGraphML("interactions_graph_-me_with_my_friends_and_their_friends");
		System.out.println(" ok.");
		((GephiInteractionsGraphGenerator)interactionsGraphGenerator).testGraph();
		System.out.println("");
	}*/
	

	
//	private final DataParser dataParser;
//	private final FriendshipsGraphGenerator friendshipsGraphGenerator;
//	private final GraphExporter graphExporter;
//	
//	private final CacheUtils cacheUtils;
//	private final InteractionsGraphGenerator interactionsGraphGenerator;
//	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	static private Map<Options,OptionAction> optionsMap = new EnumMap<Options,OptionAction>(Options.class);
	
	@Inject
	public Main(
//			DataParser dataParser, 
//			FriendshipsGraphGenerator friendshipsGraphGenerator, 
//			InteractionsGraphGenerator interactionsGraphGenerator, 
//			GraphExporter graphExporter, 
//			CacheUtils cacheUtils,
//			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator
			) {
		
//		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
		
//		optionsMap.get
	}
	
	public static void main(String[] args) {
		
		try {
			Options.valueOf(args[0].toUpperCase()).doCommand();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*Injector injector = Guice.createInjector( new FacriModule() );
		Main main = injector.getInstance(Main.class);
		
		
		
		try {
			if (args.length == 0) {
				main.printHelp();
			} else {
				String action = args[0];				
				if (args.length == 1) {
					main.doParse();
					if (action.equals("view")) {
//						main.createFriendnessGephiGraphs(false);
//						main.createInteractionsGephiGraphs(false);
						main.createInteractionsGraphstreamGraph();
					}
					if (action.equals("graphml")) {
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
		}*/
	}
	
	void printHelp() {
		System.out.println("Facri: graphml | view | [save | read] cacheFilename");
	}
}
