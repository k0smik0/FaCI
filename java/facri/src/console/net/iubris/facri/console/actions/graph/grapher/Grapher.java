package net.iubris.facri.console.actions.graph.grapher;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.grapher.GrapherAction.UseCache;
import net.iubris.facri.grapher.generators.GraphstreamGraphGenerator;
import net.iubris.facri.grapher.generators.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;

public class Grapher {
	
	private final Map<WorldTarget,GraphstreamGraphGenerator> graphgeneratorsMap = new EnumMap<WorldTarget,GraphstreamGraphGenerator>(WorldTarget.class);
	// TODO eventually
//	private Map<AnalysisType,GraphstreamGraphGenerator> graphanalyzersMap = new EnumMap<AnalysisType,GraphstreamGraphGenerator>(AnalysisType.class);
	
	@Inject
	public Grapher(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		// TODO 
		graphgeneratorsMap.put(WorldTarget.friendships, null);
		graphgeneratorsMap.put(WorldTarget.interactions, graphstreamInteractionsGraphGenerator);
	}
	
	public void execute(WorldTarget worldTarget, GraphType graphType, UseCache useCache, String filenamePrefix) throws IOException {
//		GraphstreamGraphGenerator graphstreamGraphGenerator = graphgeneratorsMap.get(worldTarget);
		graphType.makeGraph( graphgeneratorsMap.get(worldTarget), useCache, filenamePrefix );
	}
	
//	public GraphstreamGraphGenerator getGraphgenerator(WorldTarget worldTarget) {
//		return graphgeneratorsMap.get(worldTarget);
//	}
	
	public enum WorldTarget {
		friendships {
			@Override
			public Graph prepareGraph(GraphsHolder graphHolder, UseCache useCache) throws IOException {
				// TODO Auto-generated method stub
				return null;
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
	public enum WorldTargetChar implements ConsoleCommand {
		f("graphs friendships 'world'") {
			@Override
			public WorldTarget getWorldTarget() {
				return WorldTarget.friendships;
			}
		}
		,i("graphs interactions 'world'") {
			@Override
			public WorldTarget getWorldTarget() {
				return WorldTarget.interactions;
			}
		};
		private WorldTargetChar(String helpMessageCore) {
			this.helpMessage = ConsoleCommand.Utils.getPrefix(this,3)+helpMessageCore+"\n";
		}
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
		
		public abstract WorldTarget getWorldTarget();
	}
	
	public enum GraphType {
		me_and_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateMeWithMyFriends();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateMyFriends();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,my_friends_with_their_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
//				graph.display().getDefaultView().setVisible(false);
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
				handleWritingToCache(useCache, graph, filename);				
			}
		}
		,friends_of_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateFriendOfFriends();
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
				}
				handleWritingToCache(useCache, graph, filename);
			}
		};

		/*me_and_my_friends {
			@Override
			public void makeGraph() throws IOException {
				
			} 
		}
		,my_friends { 
			@Override
			public void makeGraph() throws IOException {
				
			}
		}
		,my_friends_with_their_friends { 
			@Override
			public void makeGraph() throws IOException {
				
			}
		}
		,friends_of_my_friends { 
			@Override
			public void makeGraph() throws IOException {
				
			}
		}
		,me_and_my_friends_and_friends_of_my_friends { 
			@Override
			public void makeGraph() {
				Graph graph = graphstreamGraphGenerator.getGraph();
//				if ( ! handleReadingFromCache( graph ) )
//					graphstreamGraphGenerator.generateMeWithMyFriendsAndTheirFriends();
//				handleWritingToCache(graph);
			}
		};*/
		
		
		public abstract void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException;
		
		/*public abstract void makeGraph() throws Exception;
		
		private String cacheFilename;
		private UseCache useCache;
		private GraphstreamGraphGenerator graphstreamGraphGenerator;
		
		public void setDependencies(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String cacheFilenamePrefix) {
			this.graphstreamGraphGenerator = graphstreamGraphGenerator;
			this.useCache = useCache;
			this.cacheFilename = cacheFilenamePrefix+"_-_"+name();
		}*/
		
		private static String getFilename(String prefix, String core) {
			String filename = prefix+"_-_"+core;
			return filename;
		}
		
		private static boolean handleReadingFromCache(UseCache useCache, Graph graph, String cacheFilename) throws IOException {
			if (useCache.read) {
				useCache.read(cacheFilename+"."+useCache.getCacheFileExtension(), graph);
				return true;
			}
			return false;
		}
		private static void handleWritingToCache(UseCache useCache, Graph graph, String cacheFilename) throws IOException {
			if (useCache.write)
				useCache.write(cacheFilename+"."+useCache.getCacheFileExtension(), graph);
		}
	}
	public enum GrapherTypeChar implements ConsoleCommand {
		mf("me and my friends") {
			@Override
			public GraphType getAnalysisType() {
				return GraphType.me_and_my_friends;
			}
		}
		,f("my friends") {
			@Override
			public GraphType getAnalysisType() {
				return GraphType.my_friends;
			}
		}
		,ft("my friends and their friends (friends of friends)") {
			@Override
			public GraphType getAnalysisType() {
				return GraphType.my_friends_with_their_friends;
			}
		}
		,t("friends of my friends") {
			@Override
			public GraphType getAnalysisType() {
				return GraphType.friends_of_my_friends;
			}
		}
		,mft("me, my friends, their friends") {
			@Override
			public GraphType getAnalysisType() {
				return GraphType.me_and_my_friends_and_friends_of_my_friends;
			}
		};
		
		GrapherTypeChar(String helpMessageCore) {
			this.helpMessage = ConsoleCommand.Utils.getPrefix(this,3)+helpMessageCore+"\n";
		}
		public abstract GraphType getAnalysisType();
		
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
}