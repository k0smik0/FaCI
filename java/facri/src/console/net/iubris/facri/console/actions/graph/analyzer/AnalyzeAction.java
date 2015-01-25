package net.iubris.facri.console.actions.graph.analyzer;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.grapher.GrapherAction.GrapherCommand;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.Camera;

public class AnalyzeAction implements CommandAction {
	
	private final GraphsHolder graphsHolder;
	
	@Inject
	public AnalyzeAction(GraphsHolder graphsHolder) {
		this.graphsHolder = graphsHolder;
	}

	@Override
	public void exec(Console console, String... params) throws IOException {
//		analyzer.
		if (params==null || (params.length<1)) {
			handleError(console, WRONG_ARGUMENTS_NUMBER);
			return;
		}
		
		try {
			GraphTypeCommand arg = GraphTypeCommand.valueOf( params[0] );
			
			String meUid = graphsHolder.getWorld().getMyUser().getUid();
			switch(arg) {
				case f:
					/*if (!graphsHolder.isFriendshipsGraphCreated()) {
						console.printf("graph not existant; create it first using 'f'\n");
						break;
					}*/
					
					Graph friendshipsGraph = graphsHolder.getFriendshipsGraph();
					analyze(friendshipsGraph, graphsHolder::isFriendshipsGraphCreated, false, null, meUid, graphsHolder.getFriendshipsGraphViewer(), console);
					
					break;
				case i:
					/*if (!graphsHolder.isInteractionsGraphCreated()) {
						console.printf("graph not existant; create it first using 'g'\n");
						break;
					}*/
						
					Graph interactionsGraph = graphsHolder.getInteractionsGraph();
					analyze(interactionsGraph, graphsHolder::isInteractionsGraphCreated, true, "ui.size", meUid, graphsHolder.getInteractionsGraphViewer(), console);
					break;
				default:
					console.printf( arg.getHelpMessage() );
			}
		} catch (IllegalArgumentException e) {
			handleError(console, WRONG_ARGUMENT);
		}
	}
	
	@FunctionalInterface
	interface AnalyzeFunction {
		boolean check();
	}
	
	private void analyze(Graph graph, AnalyzeFunction existantGraphChecker, boolean isDirected, String weightAttributeName, String meUid, Viewer viewer, Console console) throws IOException {
//		System.out.println(existantGraphChecker.check());
		if (!existantGraphChecker.check()) {
//		if (!graphsHolder.isFriendshipsGraphCreated()) {
			console.printf("graph not existant; create it first using '"+GrapherCommand.G.name()+"'\n");
			return;
		}
//		Graph graph = graphsHolder.getFriendshipsGraph();
		Node egoNode = graph.getNode(meUid);
		GraphstreamAnalyzer graphstreamAnalyzer = new GraphstreamAnalyzer( graph, egoNode );
		graphstreamAnalyzer.numericalAnalysis(isDirected,weightAttributeName);
		graphstreamAnalyzer.graphicalAnalysis();
		
		Camera camera = viewer.getDefaultView().getCamera();
		camera.setAutoFitView(true);
		camera.setViewPercent(0.7);
		viewer.enableAutoLayout();
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
