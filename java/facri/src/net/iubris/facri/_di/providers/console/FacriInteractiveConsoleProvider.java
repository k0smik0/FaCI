package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri.console.actions.AnalyzeAction;
import net.iubris.facri.console.actions.HelpAction;
import net.iubris.facri.console.actions.QuitAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider(QuitAction quitAction, HelpAction helpAction, AnalyzeAction analyzeAction) {
		this.interactiveConsole = new InteractiveConsole(
//				quitAction, helpAction, analyzeAction
				new CommandWithAction(QuitAction.QuitCommand.q, quitAction),
				new CommandWithAction(HelpAction.HelpCommand.h, helpAction),
				new CommandWithAction(AnalyzeAction.AnalyzeCommand.a, analyzeAction)
				);
//		QuitCommand quitCommand = QuitAction.QuitCommand;
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
