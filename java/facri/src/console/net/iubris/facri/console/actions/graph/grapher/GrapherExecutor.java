package net.iubris.facri.console.actions.graph.grapher;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.utils.UseCache;
import net.iubris.facri.grapher.generators.GraphstreamGraphGenerator;
import net.iubris.facri.grapher.generators.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;

public class GrapherExecutor {
	
	private final Map<GraphType,GraphstreamGraphGenerator> graphgeneratorsMap = new EnumMap<GraphType,GraphstreamGraphGenerator>(GraphType.class);
	// TODO eventually
//	private Map<AnalysisType,GraphstreamGraphGenerator> graphanalyzersMap = new EnumMap<AnalysisType,GraphstreamGraphGenerator>(AnalysisType.class);
	
	@Inject
	public GrapherExecutor(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		// TODO add friendships generator
		graphgeneratorsMap.put(GraphType.friendships, null);
		graphgeneratorsMap.put(GraphType.interactions, graphstreamInteractionsGraphGenerator);
	}
	
	public void execute(GraphType worldTarget, WorldType graphType, UseCache useCache, String filenamePrefix) throws IOException {
//		GraphstreamGraphGenerator graphstreamGraphGenerator = graphgeneratorsMap.get(worldTarget);
		graphType.makeGraph( graphgeneratorsMap.get(worldTarget), useCache, filenamePrefix );
	}
	
	public enum GraphType {
		friendships {
			@Override
			public Graph prepareGraph(GraphsHolder graphHolder, UseCache useCache) throws IOException {
				graphHolder.prepareForDisplayFriendships();
				Graph graph = graphHolder.getFriendshipsGraph();
				return graph;
			}
		},
		interactions {
			@Override
			public Graph prepareGraph(GraphsHolder graphHolder, UseCache useCache) throws IOException {
				graphHolder.prepareForDisplayInteractions();
//				graphHolder.hideInteractionsGraph();
				Graph graph = graphHolder.getInteractionsGraph();
				return graph;
			}			
		};
		public abstract Graph prepareGraph(GraphsHolder graphHolder, UseCache useCache) throws IOException;
	}
	public enum GraphTypeCommand implements ConsoleCommand {
		f("friendships") {
			@Override
			public GraphType getWorldTarget() {
				return GraphType.friendships;
			}
		}
		,i("interactions") {
			@Override
			public GraphType getWorldTarget() {
				return GraphType.interactions;
			}
		};
		private GraphTypeCommand(String helpMessageCore) {
			this.helpMessage = 
//					ConsoleCommand.Utils.
					getPrefix(this,3)+helpMessageCore+"\n";
		}
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
		
		public abstract GraphType getWorldTarget();
	}
	
	public enum WorldType {
		me_and_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				/*String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMeWithMyFriends();
					graphstreamGraphGenerator.doneGraphGeneration();
				}
//				Graph graph = graphstreamGraphGenerator.getGraph();
				handleWritingToCache(useCache, graph, filename);
//				graphs
*/				
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateMeWithMyFriends,
						graphstreamGraphGenerator::doneGraphGeneration,
//						()->graphstreamGraphGenerator.generateMeWithMyFriendsAndTheirFriends(),
						name(), useCache, filenamePrefix);
			}
		}
		,my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMyFriends();
					graphstreamGraphGenerator.doneGraphGeneration();
				}
//				Graph graph = graphstreamGraphGenerator.getGraph();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,my_friends_with_their_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
//				graph.display().getDefaultView().setVisible(false);
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
					graphstreamGraphGenerator.doneGraphGeneration();
				}
//				Graph graph = graphstreamGraphGenerator.getGraph();
				handleWritingToCache(useCache, graph, filename);				
			}
		}
		,friends_of_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateFriendOfFriends();
					graphstreamGraphGenerator.doneGraphGeneration();
				}
//				Graph graph = graphstreamGraphGenerator.getGraph();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,me_and_my_friends_and_friends_of_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMeWithMyFriendsAndTheirFriends();
					graphstreamGraphGenerator.doneGraphGeneration();
				}
//				Graph graph = graphstreamGraphGenerator.getGraph();
				handleWritingToCache(useCache, graph, filename);
			}
		};
		
//		private void doFunc() {
//			
//		}
		
		public abstract void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException;
		
		public static String getFilename(String prefix, String core) {
			String filename = prefix+"_-_"+core;
			return filename;
		}
		
		public static boolean handleReadingFromCache(UseCache useCache, Graph graph, String cacheFilename) throws IOException {
			if (useCache.read) {
				useCache.read(cacheFilename+"."+useCache.getCacheFileExtension(), graph);
				return true;
			}
			return false;
		}
		public static void handleWritingToCache(UseCache useCache, Graph graph, String cacheFilename) throws IOException {
			if (useCache.write)
				useCache.write(cacheFilename+"."+useCache.getCacheFileExtension(), graph);
		}
	}
	public enum WorldTypeCommand implements ConsoleCommand {
		mf("me and my friends") {
			@Override
			public WorldType getAnalysisType() {
				return WorldType.me_and_my_friends;
			}
		}
		,f("my friends") {
			@Override
			public WorldType getAnalysisType() {
				return WorldType.my_friends;
			}
		}
		,ft("my friends and their friends (friends of friends)") {
			@Override
			public WorldType getAnalysisType() {
				return WorldType.my_friends_with_their_friends;
			}
		}
		,t("friends of my friends") {
			@Override
			public WorldType getAnalysisType() {
				return WorldType.friends_of_my_friends;
			}
		}
		,mft("me, my friends, their friends") {
			@Override
			public WorldType getAnalysisType() {
				return WorldType.me_and_my_friends_and_friends_of_my_friends;
			}
		};
		
		WorldTypeCommand(String helpMessageCore) {
			this.helpMessage = 
//					ConsoleCommand.Utils.
					getPrefix(this,3)+helpMessageCore+"\n";
		}
		public abstract WorldType getAnalysisType();
		
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
	
	@FunctionalInterface
	interface GraphGenerationFunction {
		void exec();
	}
	@FunctionalInterface
	interface GraphGenerationDoneFunction {
		void exec();
	}
	public static class GraphGeneratorExecutor {
		public static void exec(Graph graph, 
//				GraphstreamGraphGenerator graphstreamGraphGenerator, 
				GraphGenerationFunction graphGeneratorFunction,
				GraphGenerationDoneFunction graphGeneratorDoneFunction,
				String worldType, UseCache useCache, String filenamePrefix) throws IOException {
			
			String filename = WorldType.getFilename(filenamePrefix, worldType);
			if ( ! WorldType.handleReadingFromCache(useCache, graph, filename) ) {
//				graphstreamGraphGenerator.generateMeWithMyFriends();
				graphGeneratorFunction.exec();
				graphGeneratorDoneFunction.exec();
//				graphstreamGraphGenerator.doneGraphGeneration();
			}
//			Graph graph = graphstreamGraphGenerator.getGraph();
			WorldType.handleWritingToCache(useCache, graph, filename);
//			graphs
		}		
	} 
}