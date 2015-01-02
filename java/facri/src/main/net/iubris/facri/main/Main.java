package net.iubris.facri.main;

import java.util.HashMap;
import java.util.Map;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.main.options.OptionAction;
import net.iubris.facri.main.options.OptionActionParse;
import net.iubris.facri.main.options.OptionActionViewByGraphstream;
import net.iubris.facri.main.options.OptionArgument;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
	
	
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
	

	
	
	Map<String,Class<? extends OptionAction>> optionsMap = new /*Enum*/HashMap<String/*OptionArgument*/,Class<? extends OptionAction>>(/*OptionArgument.class*/);
	
//	@Inject
	public Main(
//			DataParser dataParser, 
//			FriendshipsGraphGenerator friendshipsGraphGenerator, 
//			InteractionsGraphGenerator interactionsGraphGenerator, 
//			GraphExporter graphExporter, 
//			CacheUtils cacheUtils,
//			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator
			) {
		optionsMap.put(OptionArgument.PARSE.name(), OptionActionParse.class);
		optionsMap.put(OptionArgument.VIEW.name(), OptionActionViewByGraphstream.class);
	}
	
	public static void main(String[] args) {
		
		Main main = new Main();
		String arg = args[0].toUpperCase();
//		OptionArgument optionArgument = OptionArgument.valueOf(arg);
//		Option
		try {
			if (main.optionsMap.containsKey( arg )) {
				Class<? extends OptionAction> actionClass = main.optionsMap.get(arg);
				Injector injector = Guice.createInjector( new FacriParserModule() );
				OptionAction optionAction = injector.getInstance(actionClass);
				optionAction.execute();
			} else {
				OptionArgument.HELP.doCommand();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
/*		
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
	
//	void printHelp() {
//		System.out.println("Facri: graphml | view | [save | read] cacheFilename");
//	}
}
