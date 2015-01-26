package net.iubris.facri.console.actions.graph.analyzer;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.grapher.GrapherAction.GrapherCommand;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.world.World;
import net.iubris.facri.utils.Printer;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.Camera;

public class AnalyzeAction implements CommandAction {
	
	private final GraphsHolder graphsHolder;
	private final World world;
	
	@Inject
	public AnalyzeAction(World world, GraphsHolder graphsHolder) {
		this.world = world;
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
			Ego myUser = world.getMyUser();
			if (myUser==null) {
				Printer.println("no data to graph/analyze: are you created graphs using 'g'?");
				return;
			}
			String meUid = myUser.getUid();
			switch(arg) {
				case f:
					Graph friendshipsGraph = graphsHolder.getFriendshipsGraph();
					analyze(friendshipsGraph, graphsHolder::isFriendshipsGraphCreated, false, null, meUid, graphsHolder.getFriendshipsGraphViewer(), console);
					break;
				case i:
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
		if (!existantGraphChecker.check()) {
			console.printf("graph not existant; create it first using '"+GrapherCommand.G.name()+"'\n");
			return;
		}
		Node egoNode = graph.getNode(meUid);
		GraphstreamAnalyzer graphstreamAnalyzer = new GraphstreamAnalyzer( graph, egoNode );
		graphstreamAnalyzer.numericalAnalysis(isDirected,weightAttributeName);
		graphstreamAnalyzer.graphicalAnalysis();
		
		Camera camera = viewer.getDefaultView().getCamera();
		camera.setAutoFitView(true);
		camera.setViewPercent(0.9);
		viewer.enableAutoLayout();
		camera.resetView();
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
