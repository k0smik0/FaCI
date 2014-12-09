package net.iubris.facri.console.actions;

import java.io.Console;
import java.util.List;

import javax.inject.Inject;

import net.iubris.facri.console.actions.AnalyzerLocator.AnalysisType;
import net.iubris.facri.console.actions.AnalyzerLocator.WorldTarget;
import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.parsers.DataParser;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;

public class AnalyzeAction implements CommandAction {
	
//	private final static String WORLD_TARGET_FRIENDSHIPS = "f";
//	private final static String WORLD_TARGET_INTERACTIONS = "i";
	
	private final static String WRONG_ARGUMENTS_NUMBER = "analyzer needs two arguments; type 'h' for help\n";
	private final static String WORLD_TARGET_WRONG_ARGUMENT = "wrong 'world' target; type 'h' for help";
	
	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	private final DataParser dataParser;
//	
	@Inject
	public AnalyzeAction(
			DataParser dataParser,
			// missing friendships generator
			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator
			) {
		this.dataParser = dataParser;
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}

	@Override
	public void exec(Console console, List<String> params) throws Exception {
		try {
			if (params!=null && params.size()==0) {
				handleError(console);
				return;
			}
//			else if(params.size()==0) {
//				handleError(console);
////				return;
//			}
			
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
			case friendships:
				// TODO use friendships generator
//				worldTarget.makeGraph(null);
				
				break;
			case interactions:
				
				dataParser.parse();
				
				graphstreamInteractionsGraphGenerator.prepareForDisplay();
				graph = 
//						worldTarget.makeGraph(graphstreamInteractionsGraphGenerator);
				graphstreamInteractionsGraphGenerator.getGraph();
//				new GraphstreamInteractionsAnalyzer(graph, graphstreamInteractionsGraphGenerator.getEgoNode() );
				break;
			default:
				console.printf(WORLD_TARGET_WRONG_ARGUMENT);
				break;
			}

//			analysisType.makeAnalysis(graph);
			switch (analysisType) {
			case	me_and_my_friends:
				analysisType.makeAnalysis(graphstreamInteractionsGraphGenerator);
				break;
			}
				
			
		} catch (IllegalArgumentException e) {
			handleError(console);
		} /*catch( Exception e) {
			exceptionListener.handleException(e);
		}*/
		
	}
	
	private void handleError(Console console) {
		console.printf(WRONG_ARGUMENTS_NUMBER);
	}
	
	public enum AnalyzeCommand implements ConsoleCommand {
		a;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message =  HelpAction.tab(1)+"'"+name()+": analyze [world] [analysis type]"
			+HelpAction.tab(2)+"analyze command needs two arguments:\n"
			+HelpAction.tab(2)+"first argument select 'world' to analyze:\n"
	//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.f +"': analyze friendships 'world'").append(newLine)
	//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.i+"': analyze interactions 'world'").append(newLine)
			+HelpAction.tab(3)+AnalyzerLocator.WorldTargetChar.f.getHelpMessage()+"\n"
			+HelpAction.tab(3)+AnalyzerLocator.WorldTargetChar.i.getHelpMessage()+"\n"
			+HelpAction.tab(2)+"second argument select analysis type:"+"\n"
			+HelpAction.tab(3)+"'"+ AnalyzerLocator.AnalysisTypeChar.mf +"': me and my friends"+"\n"
			+HelpAction.tab(3)+"'"+ AnalyzerLocator.AnalysisTypeChar.ft +"': my friends and their friends (friends of friends)"+"\n"
			+HelpAction.tab(3)+"'"+ AnalyzerLocator.AnalysisTypeChar.t +"': friends of my friends"+"\n"
			+HelpAction.tab(3)+"'"+ AnalyzerLocator.AnalysisTypeChar.mft +"': me, my friends, their friends"+"\n"
			+HelpAction.tab(1)+"example: 'a i mf' -> analyze interactions between me and my friends"+"\n";
	} 

}
