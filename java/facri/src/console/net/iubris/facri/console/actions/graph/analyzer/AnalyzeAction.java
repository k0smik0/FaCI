package net.iubris.facri.console.actions.graph.analyzer;

import java.io.Console;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.util.Camera;

public class AnalyzeAction implements CommandAction {
	
	private final GraphsHolder graphsHolder;
	
	@Inject
	public AnalyzeAction(GraphsHolder graphsHolder) {
		this.graphsHolder = graphsHolder;
	}

	@Override
	public void exec(Console console, String... params) {
//		analyzer.
		if (params==null || (params.length<1)) {
			handleError(console, WRONG_ARGUMENTS_NUMBER);
			return;
		}
		
//		if (!graphsHolder.isGraphsCreated())
//			console.printf("graphs not existant; create them first using 'g'\n");
		try {
			GraphTypeCommand arg = GraphTypeCommand.valueOf( params[0] );
			
//			UseCache.handleUseCache(params, dataParser);
			String meUid = graphsHolder.getWorld().getMyUser().getUid();
			Graph graph = null;
			switch(arg) {
				case f:
					if (!graphsHolder.isFriendshipsGraphCreated()) {
						console.printf("graph not existant; create it first using 'g'\n");
						break;
					}
					graph = graphsHolder.getFriendshipsGraph();
					Node egoNodeInFriendships = graph.getNode(meUid);
					GraphstreamAnalyzer friendshipsGraphstreamAnalyzer = new GraphstreamAnalyzer( graphsHolder.getFriendshipsGraph(), egoNodeInFriendships );
					friendshipsGraphstreamAnalyzer.numericalAnalysis(false,null);
					friendshipsGraphstreamAnalyzer.graphicalAnalysis();
					break;
				case i:
					if (!graphsHolder.isInteractionsGraphsCreated()) {
						console.printf("graph not existant; create it first using 'g'\n");
						break;
					}
					graph = graphsHolder.getInteractionsGraph();
					Node egoNodeInInteractions = graph.getNode(meUid);
					GraphstreamAnalyzer interactionsGraphstreamAnalyzer = new GraphstreamAnalyzer( graphsHolder.getInteractionsGraph(), egoNodeInInteractions );
					interactionsGraphstreamAnalyzer.numericalAnalysis(true,"interactions");
					interactionsGraphstreamAnalyzer.graphicalAnalysis();
					
					Camera camera = graphsHolder.getInteractionsGraphViewer().getDefaultView().getCamera();
					camera.setAutoFitView(true);
					camera.setViewPercent(0.3);
//					Node firstNodeWithMaximumDegreeExceptEgo = interactionsGraphstreamAnalyzer.getFirstNodeWithMaximumDegreeExceptEgo();
//					Double[] coordinates = (Double[]) firstNodeWithMaximumDegreeExceptEgo.getArray("xy");
//					camera.setViewCenter(coordinates[0], coordinates[1], 0);
					break;
				default:
					console.printf( arg.getHelpMessage() );
			}
		} catch (IllegalArgumentException e) {
			handleError(console, WRONG_ARGUMENT);
		}
	}
	
	public enum AnalyzeCommand implements ConsoleCommand {
		A;
		final private String helpMessage;
		private AnalyzeCommand() {
			String helpMessageSuffix = "";
			for (GraphTypeCommand gtc: GraphTypeCommand.values()) {
				helpMessageSuffix += gtc.getHelpMessage();
			}
			this.helpMessage = 
				"analyze [graph_type]\n"
			+HelpAction.tab(2)+"[graph_type] are:\n"
			+helpMessageSuffix;
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}		
	}

}
