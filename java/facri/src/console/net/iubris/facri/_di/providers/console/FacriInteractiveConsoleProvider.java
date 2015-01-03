package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri.console.actions.graph.analyzer.AnalyzeAction;
import net.iubris.facri.console.actions.graph.grapher.GraphCSSAction;
import net.iubris.facri.console.actions.graph.grapher.GrapherAction;
import net.iubris.facri.console.actions.graph.layout.GraphLayoutAction;
import net.iubris.facri.console.actions.graph.search.SearchUserInGraphAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;
import net.iubris.heimdall.actions.QuitAction;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider(QuitAction quitAction, GrapherAction grapherAction, GraphCSSAction graphCSSAction, GraphLayoutAction graphLayoutAction ,SearchUserInGraphAction searchUserInGraphAction, AnalyzeAction analyzeAction) {
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction)
				,new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction)
				,new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction)
				,new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction)
//				new CommandWithAction(GraphLayoutAction.GraphLayoutCommand.L, graphLayoutAction),
//				,new CommandWithAction(QuitAction.QuitCommand.Q, quitAction)
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
