package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri._test.GraphCSSAction;
import net.iubris.facri.console.actions.AnalyzeAction;
import net.iubris.facri.console.actions.QuitAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider(QuitAction quitAction, AnalyzeAction analyzeAction, GraphCSSAction graphCSSAction) {
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				new CommandWithAction(QuitAction.QuitCommand.Q, quitAction),
				new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction),
				new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction)
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
