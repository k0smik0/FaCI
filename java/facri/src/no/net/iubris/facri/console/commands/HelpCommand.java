package net.iubris.facri.console.commands;

import net.iubris.facri.console.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

public enum HelpCommand implements ConsoleCommand {
	h;
	@Override
	public String getHelpMessage() {
		return helpMessage;
	}
	private String helpMessage = HelpAction.tab(1)+"'"+name()+"': display this help\n";
}