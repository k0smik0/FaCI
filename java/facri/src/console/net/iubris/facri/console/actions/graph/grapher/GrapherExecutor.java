/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GrapherExecutor.java) is part of facri.
 * 
 *     GrapherExecutor.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GrapherExecutor.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console.actions.graph.grapher;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler;
import net.iubris.facri.grapher.generators.friendships.graphstream.GraphstreamFriendshipsGraphGenerator;
import net.iubris.facri.grapher.generators.graphstream.GraphstreamGraphGenerator;
import net.iubris.facri.grapher.generators.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;

public class GrapherExecutor {
	
	private final Map<GraphType,GraphstreamGraphGenerator> graphgeneratorsMap = new EnumMap<GraphType,GraphstreamGraphGenerator>(GraphType.class);
	
//	public static final String graph_file_name = "graph_file_name";
//	private static String myUserId = "";
	
	@Inject
	public GrapherExecutor(GraphstreamFriendshipsGraphGenerator graphstreamFriendshipsGraphGenerator, GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator/*, @Named("my_user_id") String myUserId*/) {
		graphgeneratorsMap.put(GraphType.friendships, graphstreamFriendshipsGraphGenerator);
		graphgeneratorsMap.put(GraphType.interactions, graphstreamInteractionsGraphGenerator);
//		GrapherExecutor.myUserId = myUserId;
	}
	
	public void execute(GraphType graphType, WorldType worldType, CacheHandler useCache) throws IOException, JAXBException, XMLStreamException {
		GraphstreamGraphGenerator graphstreamGraphGenerator = graphgeneratorsMap.get(graphType);
		String graphtypeAsFilenamePrefix = graphType.name();
		worldType.makeGraph( graphstreamGraphGenerator, useCache, graphtypeAsFilenamePrefix );
	}
	
	public enum GraphType {
		friendships {
			@Override
			public GraphType prepareGraph(GraphsHolder graphHolder) throws IOException {
				graphHolder.prepareForDisplayFriendships();
				return this;
			}
		},
		interactions {
			@Override
			public GraphType prepareGraph(GraphsHolder graphHolder) throws IOException {
				graphHolder.prepareForDisplayInteractions();
				return this;
			}			
		};
		public abstract GraphType prepareGraph(GraphsHolder graphHolder) throws IOException;
	}
	public enum GraphTypeCommand implements ConsoleCommand {
		f("friendships") {
			@Override
			public GraphType getGraphType() {
				return GraphType.friendships;
			}
		}
		,i("interactions") {
			@Override
			public GraphType getGraphType() {
				return GraphType.interactions;
			}
		};
		private GraphTypeCommand(String helpMessageCore) {
			this.helpMessage = getPrefix(this,3)+helpMessageCore+"\n";
		}
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
		
		public abstract GraphType getGraphType();
	}
	
	public enum WorldType {
		me_and_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException {
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateMeWithMyFriends,
						graphstreamGraphGenerator::setGraphAsGenerated,
						useCache,
						WorldType.getFilename(filenamePrefix, name())
					);
			}
		}
		,my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException {
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateMyFriends,
						graphstreamGraphGenerator::setGraphAsGenerated,
						useCache,
						WorldType.getFilename(filenamePrefix, name())
					);
			}
		}
		,my_friends_with_their_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException {
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateMyFriendsAndFriendOfFriends,
						graphstreamGraphGenerator::setGraphAsGenerated,
						useCache,
						WorldType.getFilename(filenamePrefix, name())
					);
			}
		}
		,friends_of_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException {
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateFriendOfFriends,
						graphstreamGraphGenerator::setGraphAsGenerated,
						useCache,
						WorldType.getFilename(filenamePrefix, name())
					);
			}
		}
		,me_and_my_friends_and_friends_of_my_friends {
			@Override
			public void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException {
				GraphGeneratorExecutor.exec(graphstreamGraphGenerator.getGraph(),
						graphstreamGraphGenerator::generateMeWithMyFriendsAndTheirFriends,
						graphstreamGraphGenerator::setGraphAsGenerated,
						useCache,
						WorldType.getFilename( filenamePrefix, name() )
				);
			}
		};
		
		public abstract void makeGraph(GraphstreamGraphGenerator graphstreamGraphGenerator, CacheHandler useCache, String filenamePrefix) throws IOException, JAXBException, XMLStreamException;
		
		private static String getFilename(String prefix, String core) {
			String filename = prefix+"_-_"+core;
			return filename;
		}
	}
	public enum WorldTypeCommand implements ConsoleCommand {
		mf("me and my friends") {
			@Override
			public WorldType getWorldType() {
				return WorldType.me_and_my_friends;
			}
		}
		,f("my friends") {
			@Override
			public WorldType getWorldType() {
				return WorldType.my_friends;
			}
		}
		,ft("my friends and their friends (friends of friends)") {
			@Override
			public WorldType getWorldType() {
				return WorldType.my_friends_with_their_friends;
			}
		}
		,t("friends of my friends") {
			@Override
			public WorldType getWorldType() {
				return WorldType.friends_of_my_friends;
			}
		}
		,mft("me, my friends, their friends") {
			@Override
			public WorldType getWorldType() {
				return WorldType.me_and_my_friends_and_friends_of_my_friends;
			}
		};
		
		WorldTypeCommand(String helpMessageCore) {
			this.helpMessage = 
					getPrefix(this,3)+helpMessageCore+"\n";
		}
		public abstract WorldType getWorldType();
		
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
	
	@FunctionalInterface
	public interface GraphGenerationFunction {
		void generate();
	}
	@FunctionalInterface
	public interface GraphGenerationDoneFunction {
		void setGenerated();
	}
	public static class GraphGeneratorExecutor {
		public static void exec(Graph graph, 
				GraphGenerationFunction graphGeneratorFunction,
				GraphGenerationDoneFunction graphGeneratorDoneFunction,
				CacheHandler cacheHandler, String fileBasename) throws IOException, JAXBException, XMLStreamException {

//			graph.addAttribute(graph_file_name, myUserId+"_-_"+fileBasename);
			
			cacheHandler.exec(graph,
					fileBasename,
					graphGeneratorFunction,
					graphGeneratorDoneFunction);
		}		
	}
}
