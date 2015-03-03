/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (AnalyzeAction.java) is part of facri.
 * 
 *     AnalyzeAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     AnalyzeAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.console.actions.graph.analyzer;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.faci.analyzer.graphstream.GraphstreamAnalyzer;
import net.iubris.faci.analyzer.graphstream.GraphstreamAnalyzer.AnalysisTypeShortcut.Graphical;
import net.iubris.faci.analyzer.graphstream.GraphstreamAnalyzer.AnalysisTypeShortcut.Numerical;
import net.iubris.faci.analyzer.graphstream.GraphstreamAnalyzerSuffix.GraphstreamAnalyzerSuffixFactory;
import net.iubris.faci.console.actions.graph.grapher.GrapherAction.GrapherCommand;
import net.iubris.faci.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.faci.grapher.cloner.GraphCloner;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.utils.Printer;
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
	private final GraphstreamAnalyzerSuffixFactory graphstreamAnalyzerFactory;
	private boolean excludingEgo = false;
	
	@Inject
	public AnalyzeAction(World world, GraphsHolder graphsHolder, GraphstreamAnalyzerSuffixFactory graphstreamAnalyzerFactory) {
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
			if (params.length==3)
				this.excludingEgo  = true;
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
		
		if (!excludingEgo)
			doAnalysisIncludingEgo(graph, egoNode, analyzeTypeParam, isDirected, weightAttributeName, viewer);
		else
			doAnalysisExcludingEgo(graph, egoNode, analyzeTypeParam, isDirected, weightAttributeName, viewer);
	}

	
	
	
	// TODO improve
	private void doAnalysisIncludingEgo(Graph graph, Node egoNode, String analyzeTypeParam, boolean isDirected, String weightAttributeName, Viewer viewer) throws IOException {
		GraphstreamAnalyzer graphstreamAnalyzerWithEgo = graphstreamAnalyzerFactory.create( graph, egoNode, "" );
		doAnalysis(analyzeTypeParam, isDirected, weightAttributeName, graphstreamAnalyzerWithEgo, viewer);
	}
	private void doAnalysisExcludingEgo(Graph graph, Node egoNode, String analyzeTypeParam, boolean isDirected, String weightAttributeName, Viewer viewer) throws IOException {
		String suffix = "_-_without_ego";
		Graph copy = GraphCloner.copy(graph);
		Printer.println("Removed Ego node\n");
		copy.removeNode(egoNode);
		GraphstreamAnalyzer graphstreamAnalyzerWithoutEgo = graphstreamAnalyzerFactory.create( graph, egoNode, suffix );
		doAnalysis(analyzeTypeParam, isDirected, weightAttributeName, graphstreamAnalyzerWithoutEgo, viewer);
	}
	
	
	private void doAnalysis(String analyzeTypeParam, boolean isDirected, 
			String weightAttributeName, GraphstreamAnalyzer graphstreamAnalyzer, Viewer viewer) throws IOException {
		try { // try numerical
			Numerical numerical = Numerical.valueOf(analyzeTypeParam);
			if (numerical.equals(Numerical.di) || numerical.equals(Numerical.a))
				numerical.doAnalysis(graphstreamAnalyzer, new Object[] {weightAttributeName, isDirected});
			else
				numerical.doAnalysis(graphstreamAnalyzer);
		} catch (IllegalArgumentException e1) {
			try { // try graphical
				System.out.println(analyzeTypeParam);
				Graphical graphical = Graphical.valueOf(analyzeTypeParam);
				graphical.doAnalysis(graphstreamAnalyzer);
				
				Camera camera = viewer.getDefaultView().getCamera();
				camera.setAutoFitView(true);
				camera.setViewPercent(1);
				viewer.enableAutoLayout();
				camera.resetView();
			} catch (IllegalArgumentException e2) {
				Printer.println(analyzeTypeParam+": unknown analysis");
			}
		}
	}
	
	
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
			helpMessageSuffix+=HelpAction.tab(2)+"[ee]: excluding Ego user from computations\n";
			helpMessageSuffix+=HelpAction.tab(2)+"example: 'a i cl' -> search cliques within interactions graph\n";
			helpMessageSuffix+=HelpAction.tab(2)+"example: 'a i cl ee' -> search cliques within interactions graph, excluding Ego";
			
			this.helpMessage = 
				"analyze [graph_type] [analysis_type] [ee]\n"
			+helpMessageSuffix;
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}		
	}
	
	@FunctionalInterface
	interface GraphExistantCheckerFunction {
		boolean check();
	}

}
