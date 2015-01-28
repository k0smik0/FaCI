package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri.console.actions.graph.analyzer.AnalyzeAction;
import net.iubris.facri.console.actions.graph.grapher.GraphCSSAction;
import net.iubris.facri.console.actions.graph.grapher.GrapherAction;
import net.iubris.facri.console.actions.graph.layout.GraphLayoutAction;
import net.iubris.facri.console.actions.graph.nozerodegree.ClearUselessNodesAction;
import net.iubris.facri.console.actions.graph.search.SearchUserInGraphAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider( 
			GrapherAction grapherAction, 
			GraphCSSAction graphCSSAction, 
			GraphLayoutAction graphLayoutAction, 
			SearchUserInGraphAction searchUserInGraphAction, 
			AnalyzeAction analyzeAction,
			ClearUselessNodesAction clearUselessNodesAction) {
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction)
				,new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction)
				,new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction)
				,new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction)
				,new CommandWithAction(ClearUselessNodesAction.ClearUselessNodesActionCommand.Z, clearUselessNodesAction)
//				new CommandWithAction(GraphLayoutAction.GraphLayoutCommand.L, graphLayoutAction),
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
