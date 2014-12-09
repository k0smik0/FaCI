package net.iubris.facri.console.actions;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.graph.generator.graphstream.GraphstreamGraphGenerator;
import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.heimdall.command.ConsoleCommand;

public class AnalyzerLocator {
	
	
	public enum WorldTarget {
		friendships,interactions;
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
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator) {
				graphstreamGraphGenerator.generateMeWithMyFriends();
			}
		}
		,my_friends_with_their_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator) {
				graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
			}
		}
		,friends_of_my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator) {
				// TODO Auto-generated method stub
//				graphstreamGraphGenerator.ge
			}
		}
		,my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator) {
				graphstreamGraphGenerator.generateMyFriends();
			}
		}
		,me_and_my_friends_and_friends_of_my_friends {
			@Override
			public void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator) {
				graphstreamGraphGenerator.generateMyFriendsAndFriendOfFriends();
			}
		};
		
		public abstract void makeAnalysis(GraphstreamGraphGenerator graphstreamGraphGenerator);
	}
	public enum AnalysisTypeChar {
		mf {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.me_and_my_friends;
			}
		},
		f {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.my_friends;
			}
		},
		ft {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.my_friends_with_their_friends;
			}
		},
		t {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.friends_of_my_friends;
			}
		},
		mft {
			@Override
			public AnalysisType getAnalysisType() {
				return AnalysisType.me_and_my_friends_and_friends_of_my_friends;
			}
		};
		public abstract AnalysisType getAnalysisType();
	}

	private Map<WorldTarget,GraphstreamGraphGenerator> graphgeneratorsMap = new EnumMap<WorldTarget,GraphstreamGraphGenerator>(WorldTarget.class);
	// TODO eventually
//	private Map<AnalysisType,GraphstreamGraphGenerator> graphanalyzersMap = new EnumMap<AnalysisType,GraphstreamGraphGenerator>(AnalysisType.class);
	
	@Inject
	public AnalyzerLocator(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		graphgeneratorsMap.put(WorldTarget.friendships, null);
		graphgeneratorsMap.put(WorldTarget.interactions, graphstreamInteractionsGraphGenerator);
	}
	
	public void execute(WorldTarget worldTarget, AnalysisType analysisType) {
		GraphstreamGraphGenerator graphstreamGraphGenerator = graphgeneratorsMap.get(worldTarget);
		analysisType.makeAnalysis(graphstreamGraphGenerator);
	}
}
