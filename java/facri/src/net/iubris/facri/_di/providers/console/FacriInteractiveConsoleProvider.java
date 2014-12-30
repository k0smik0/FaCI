package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri.console.actions.graph.grapher.GraphCSSAction;
import net.iubris.facri.console.actions.graph.grapher.GrapherAction;
import net.iubris.facri.console.actions.graph.layout.GraphLayoutAction;
import net.iubris.facri.console.actions.graph.search.SearchUserInGraphAction;
import net.iubris.facri.console.actions.quit.QuitAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider(QuitAction quitAction, GrapherAction grapherAction, GraphCSSAction graphCSSAction, GraphLayoutAction graphLayoutAction ,SearchUserInGraphAction searchUserInGraphAction) {
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				new CommandWithAction(QuitAction.QuitCommand.Q, quitAction),
				new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction),
				new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction),
				new CommandWithAction(GraphLayoutAction.GraphLayoutCommand.L, graphLayoutAction),
				new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction)
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
