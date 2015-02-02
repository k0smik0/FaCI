/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (HelpCommand.java) is part of facri.
 * 
 *     HelpCommand.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HelpCommand.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
