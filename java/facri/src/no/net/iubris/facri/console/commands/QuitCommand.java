package net.iubris.facri.console.commands;

import net.iubris.facri.console.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

public enum QuitCommand implements ConsoleCommand {
	q;
	@Override
	public String getHelpMessage() {
		return message;
	}
	private String message = HelpAction.tab(1)+"'"+name()+"': exit\n";
}