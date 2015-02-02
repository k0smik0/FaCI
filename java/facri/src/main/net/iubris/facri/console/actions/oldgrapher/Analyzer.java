/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Analyzer.java) is part of facri.
 * 
 *     Analyzer.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Analyzer.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console.actions.oldgrapher;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.console.actions.AnalyzeAction.UseCache;
import net.iubris.facri.graph.generators.graphstream.GraphstreamGraphGenerator;
import net.iubris.facri.graph.generators.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.model.graph.GraphHolder;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;

public class Analyzer {
	
	private final Map<WorldTarget,GraphstreamGraphGenerator> graphgeneratorsMap = new EnumMap<WorldTarget,GraphstreamGraphGenerator>(WorldTarget.class);
	// TODO eventually
//	private Map<AnalysisType,GraphstreamGraphGenerator> graphanalyzersMap = new EnumMap<AnalysisType,GraphstreamGraphGenerator>(AnalysisType.class);
	
	@Inject
	public Analyzer(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		// TODO 
		graphgeneratorsMap.put(WorldTarget.friendships, null);
		graphgeneratorsMap.put(WorldTarget.interactions, graphstreamInteractionsGraphGenerator);
	}
	
	public void execute(WorldTarget worldTarget, AnalysisType analysisType, UseCache useCache, String filenamePrefix) throws IOException {
		GraphstreamGraphGenerator graphstreamGraphGenerator = graphgeneratorsMap.get(worldTarget);
		analysisType.makeAnalysis(graphstreamGraphGenerator, useCache, filenamePrefix);
	}
	
	
//	private final static String INTERACTIONS_CACHE_FILENAME = "graph_interactions.graphml"; // use graphml
	
	public enum WorldTarget {
		friendships {
			@Override
			public Graph prepareGraph(GraphHolder graphHolder, UseCache useCache) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		},
		interactions {
			@Override
			public Graph prepareGraph(GraphHolder graphHolder, UseCache useCache) throws IOException {
				graphHolder.prepareForDisplayInteractions();
				Graph graph = graphHolder.getInteractionsGraph();
				return graph;
			}			
		};
		public abstract Graph prepareGraph(GraphHolder graphHolder, UseCache useCache) throws IOException;
	}
	public enum WorldTargetChar implements ConsoleCommand {
		f {
			@Override
			public WorldTarget getWorldTarget() {
				return WorldTarget.friendships;
			}
			@Override
			public String getHelpMessage() {
				return "'"+this.name()+"': analyze friendships 'world'";
			}
		}
		,i {
			@Override
			public WorldTarget getWorldTarget() {
				return WorldTarget.interactions;
			}
			@Override
			public String getHelpMessage() {
				return "'"+this.name()+"': analyze interactions 'world'";
			}
		};
		public abstract WorldTarget getWorldTarget();
	}
	
	public enum AnalysisType {
		me_and_my_friends {
//			@Inject static GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateMeWithMyFriends();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMyFriends();
				}
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,my_friends_with_their_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) )
					graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,friends_of_my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateFriendOfFriends();
				}
				handleWritingToCache(useCache, graph, filename);
			}
		}
		,me_and_my_friends_and_friends_of_my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException {
				String filename = getFilename(filenamePrefix, name());
				Graph graph = graphstreamGraphGenerator.getGraph();
				if ( ! handleReadingFromCache(useCache, graph, filename) ) {
					graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
				}
				handleWritingToCache(useCache, graph, filename);
			}
		};
		
		public abstract void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator, UseCache useCache, String filenamePrefix) throws IOException;
		
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
		private static void handleWritingToCache(UseCache useCache, Graph graph, String filename) throws IOException {
			if (useCache.write)
				useCache.write(filename+"."+useCache.getCacheFileExtension(), graph);
		}
	}
	public enum AnalysisTypeChar {
		mf {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.me_and_my_friends;
			}
		}
		,f {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.my_friends;
			}
		}
		,ft {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.my_friends_with_their_friends;
			}
		}
		,t {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.friends_of_my_friends;
			}
		}
		,mft {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.me_and_my_friends_and_friends_of_my_friends;
			}
		};
		public abstract AnalysisType getAnalysisType();
	}
}
