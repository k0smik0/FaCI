package net.iubris.facri.console.actions;

import java.io.Console;
import java.util.List;

import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class QuitAction implements CommandAction {
	
	@Override
	public void exec(Console console, List<String> params) {
		console.printf("Bye%n");
		System.exit(0);
	}
	
	public enum QuitCommand implements ConsoleCommand {
		q;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message = HelpAction.tab(1)+"'"+name()+"': exit\n";
	}
}


