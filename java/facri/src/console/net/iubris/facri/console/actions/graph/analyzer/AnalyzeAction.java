package net.iubris.facri.console.actions.graph.analyzer;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.facri.console.actions.graph.grapher.GrapherAction.GrapherCommand;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer.AnalysisTypeShortcut.Graphical;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer.AnalysisTypeShortcut.Numerical;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer.GraphstreamAnalyzerFactory;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.world.World;
import net.iubris.facri.utils.Printer;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

public class AnalyzeAction implements CommandAction {
	
	private final GraphsHolder graphsHolder;
	private final World world;
	private final GraphstreamAnalyzerFactory graphstreamAnalyzerFactory;
	
	@Inject
	public AnalyzeAction(World world, GraphsHolder graphsHolder, GraphstreamAnalyzerFactory graphstreamAnalyzerFactory) {
		this.world = world;
		this.graphsHolder = graphsHolder;
		this.graphstreamAnalyzerFactory = graphstreamAnalyzerFactory;
	}

	@Override
	public void exec(Console console, String... params) {
		if (params==null || (params.length<2)) {
			handleError(console, WRONG_ARGUMENTS_NUMBER);
			return;
		}
		
		try {
			GraphTypeCommand arg = GraphTypeCommand.valueOf( params[0] );
			String analyzeTypeParam = params[1];
			Ego myUser = world.getMyUser();
			if (myUser==null) {
				Printer.println("no data to graph/analyze: are you created graphs using 'g'?");
				return;
			}
			String meUid = myUser.getUid();
			switch(arg) {
				case f:
					analyze(graphsHolder.getFriendshipsGraph(),
							graphsHolder::isFriendshipsGraphCreated,
							analyzeTypeParam,
							false, null, meUid, graphsHolder.getFriendshipsGraphViewer(), console);
					break;
				case i:
					analyze(graphsHolder.getInteractionsGraph(), 
							graphsHolder::isInteractionsGraphCreated,
							analyzeTypeParam,
							true, "ui.size", meUid, graphsHolder.getInteractionsGraphViewer(), console);
					break;
				default:
					console.printf( arg.getHelpMessage() );
			}
		} catch (IllegalArgumentException e) {
			handleError(console, WRONG_ARGUMENT);
		} catch (ClassNotFoundException e) {
			handleError(console, e.getMessage());
		} catch (IOException e) {
			handleError(console, e.getMessage());
		}
	}
	
	@FunctionalInterface
	interface GraphExistantCheckerFunction {
		boolean check();
	}
	
	private void analyze(Graph graph, 
			GraphExistantCheckerFunction graphExistantCheckerFunction,
			String analyzeTypeParam,
			boolean isDirected, 
			String weightAttributeName, 
			String meUid, 
			Viewer viewer, 
			Console console) throws IOException, ClassNotFoundException {
		if (!graphExistantCheckerFunction.check()) {
			console.printf("graph not existant; create it first using '"+GrapherCommand.G.name()+"'\n");
			return;
		}
		Node egoNode = graph.getNode(meUid);
		GraphstreamAnalyzer graphstreamAnalyzer = graphstreamAnalyzerFactory.create( graph, egoNode );
		
		try { // try numerical
			Numerical numerical = Numerical.valueOf(analyzeTypeParam);
			if (numerical.equals(Numerical.di))
				numerical.doAnalysis(graphstreamAnalyzer, new Object[] {weightAttributeName, isDirected});
			else
				numerical.doAnalysis(graphstreamAnalyzer);
		} catch (IllegalArgumentException e1) {
			try { // try graphical
				Graphical graphical = Graphical.valueOf(analyzeTypeParam);
				graphical.doAnalysis(graphstreamAnalyzer);
				
				Camera camera = viewer.getDefaultView().getCamera();
				camera.setAutoFitView(true);
				camera.setViewPercent(1);
				viewer.enableAutoLayout();
				camera.resetView();
			} catch (IllegalArgumentException e2) {
				console.printf(analyzeTypeParam+": unknown analysis");
			}
		}
		
//		graphstreamAnalyzer.numericalAnalysis(isDirected,weightAttributeName);
//		graphstreamAnalyzer.graphicalAnalysis();
		
//		Camera camera = viewer.getDefaultView().getCamera();
//		camera.setAutoFitView(true);
//		camera.setViewPercent(1);
//		viewer.enableAutoLayout();
//		camera.resetView();
	}
	
//	checkExistantGraph() {}
	
	public enum AnalyzeCommand implements ConsoleCommand {
		A;
		final private String helpMessage;
		private AnalyzeCommand() {
			String helpMessageSuffix = "";
			helpMessageSuffix+=HelpAction.tab(2)+"[graph_type] are:\n";
			for (GraphTypeCommand gtc: GraphTypeCommand.values()) {
				helpMessageSuffix += gtc.getHelpMessage();
			}
			helpMessageSuffix+=HelpAction.tab(2)+"[analysis_type] are:\n";
			helpMessageSuffix+=HelpAction.tab(3)+"numerical:\n";
			for (Numerical n: Numerical.values()) {
				helpMessageSuffix += HelpAction.tab(4)+"'"+n.name()+"': "+n.getHelpMessage()+"\n";
			}
			helpMessageSuffix+=HelpAction.tab(3)+"graphical:\n";
			for (Graphical g: Graphical.values()) {
				helpMessageSuffix += HelpAction.tab(4)+"'"+g.name()+"': "+g.getHelpMessage()+"\n";
			}
			helpMessageSuffix+=HelpAction.tab(2)+"example: 'a i cl' -> search cliques within interactions graph";
			this.helpMessage = 
				"analyze [graph_type] [analysis_type]\n"
			+helpMessageSuffix;
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}		
	}

}
