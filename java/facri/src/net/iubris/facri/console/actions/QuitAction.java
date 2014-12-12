package net.iubris.facri.console.actions;

import java.io.Console;

import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class QuitAction implements CommandAction {
	
	@Override
	public void exec(Console console, String... params) {
		console.printf("Bye%n");
		System.exit(0);
	}
	
	public enum QuitCommand implements ConsoleCommand {
		Q;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message = 
//				HelpAction.tab(1)+"'"+name()+
				"exit";
	}
}


