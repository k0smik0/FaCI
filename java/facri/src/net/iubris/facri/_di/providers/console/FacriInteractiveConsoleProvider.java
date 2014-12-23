package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri._test.GraphCSSAction;
import net.iubris.facri.console.actions.graph.GrapherAction;
import net.iubris.facri.console.actions.graph.SearchUserInGraphAction;
import net.iubris.facri.console.actions.quit.QuitAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider(QuitAction quitAction, GrapherAction analyzeAction, GraphCSSAction graphCSSAction, SearchUserInGraphAction searchUserInGraphAction) {
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				new CommandWithAction(QuitAction.QuitCommand.Q, quitAction),
				new CommandWithAction(GrapherAction.AnalyzeCommand.G, analyzeAction),
				new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction),
				new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction)
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
