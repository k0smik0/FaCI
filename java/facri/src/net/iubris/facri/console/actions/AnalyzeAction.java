package net.iubris.facri.console.actions;

import java.io.Console;
import java.util.List;

import javax.inject.Inject;

import net.iubris.facri.console.actions.AnalyzerLocator.AnalysisType;
import net.iubris.facri.console.actions.AnalyzerLocator.WorldTarget;
import net.iubris.facri.graph.analyzer.graphstream.GraphstreamInteractionsAnalyzer;
import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.heimdall.actions.CommandAction;

import org.graphstream.graph.Graph;

public class AnalyzeAction implements CommandAction {
	
//	private final static String WORLD_TARGET_FRIENDSHIPS = "f";
//	private final static String WORLD_TARGET_INTERACTIONS = "i";
	
	private final static String WRONG_ARGUMENTS_NUMBER = "analyzer needs two arguments; type 'h' for help";
	private final static String WORLD_TARGET_WRONG_ARGUMENT = "wrong 'world' target; type 'h' for help";
	
	/*public enum WorldTarget {
		f {
			@Override
			public Graph makeGraph(GraphGenerator graphGenerator) {
				return null;
			}
		}
		,i {
			@Override
			public Graph makeGraph(GraphGenerator graphGenerator) {
				return null;
			}
		};
		public abstract Graph makeGraph(GraphGenerator graphGenerator);
	}*/
	
	/*public enum AnalysisType {
		// Me and my Friends
		mf {
			@Override
			public void makeAnalysis(Graph graph) {
				// TODO Auto-generated method stub
			}
		},
		// (my) Friends and Their friends
		ft {
			@Override
			public void makeAnalysis(Graph graph) {
				// TODO Auto-generated method stub
			}
		},
		// friends of my friends
		tt {
			@Override
			public void makeAnalysis(Graph graph) {
				// TODO Auto-generated method stub
			}
		},
		// Me, my Friends and Their friends
		mft {
			@Override
			public void makeAnalysis(Graph graph) {
				// TODO Auto-generated method stub
			}
		};

		public abstract void makeAnalysis(Graph graph);
	}*/
	
	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
//	
	@Inject
	public AnalyzeAction(
			// missing friendships generator
			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator
			) {
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}

	@Override
	public void exec(Console console, List<String> params) throws Exception {
		try {
			if (params!=null)
				handleError(console);
			if(params.size()==0)
				handleError(console);
			
			String worldTargetParam = params.get(0);
			// WorldTarget worldTarget = Enum.valueOf(WorldTarget.class, worldTargetParam);
			WorldTarget worldTarget = Enum.valueOf(AnalyzerLocator.WorldTargetChar.class, worldTargetParam).getWorldTarget();

			String analysisTypeParam = params.get(1);
			// AnalysisType analysisType = Enum.valueOf(AnalysisType.class, analysisTypeParam);
			AnalysisType analysisType = Enum.valueOf(AnalyzerLocator.AnalysisTypeChar.class, analysisTypeParam).getAnalysisType();

			
			
//			worldTarget.
			// graphstreamInteractionsGraphGenerator.prepareForDisplay();
			Graph graph = null;
			switch (worldTarget) {
			case f:
				// TODO use friendships generator
				worldTarget.makeGraph(null);
				break;
			case i:
				graphstreamInteractionsGraphGenerator.prepareForDisplay();
				graph = worldTarget.makeGraph(graphstreamInteractionsGraphGenerator);
				new GraphstreamInteractionsAnalyzer(graph, graphstreamInteractionsGraphGenerator.getEgoNode());
				break;
			default:
				console.printf(WORLD_TARGET_WRONG_ARGUMENT);
				break;
			}

			analysisType.makeAnalysis(graph);
				
			
		} catch (IllegalArgumentException e) {
			handleError(console);
		} /*catch( Exception e) {
			exceptionListener.handleException(e);
		}*/
		
	}
	
	private void handleError(Console console) {
		console.printf(WRONG_ARGUMENTS_NUMBER);
	}

}
