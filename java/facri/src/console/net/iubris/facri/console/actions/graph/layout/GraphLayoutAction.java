/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphLayoutAction.java) is part of facri.
 * 
 *     GraphLayoutAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphLayoutAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console.actions.graph.layout;


import java.io.Console;
import javax.inject.Inject;

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.utils.Printer;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class GraphLayoutAction implements CommandAction {
	
	private final GraphsHolder graphsHolder;

	@Inject
	public GraphLayoutAction(GraphsHolder graphsHolder) {
		this.graphsHolder = graphsHolder;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		if (params==null || (params!=null && params.length<1)) {
			console.printf("wrong arguments");
			return;
		}
		
		String arg = params[0];
		switch (arg) {
		case "on":
			graphsHolder.getFriendshipsGraphViewer().enableAutoLayout();
			graphsHolder.getInteractionsGraphViewer().enableAutoLayout();
			break;
		case "off":
			graphsHolder.getFriendshipsGraphViewer().disableAutoLayout();
			graphsHolder.getInteractionsGraphViewer().disableAutoLayout();
			break;
		default:
			Printer.println( GraphLayoutCommand.L.getHelpMessage() );
		}

	}
	
	public enum GraphLayoutCommand implements ConsoleCommand {
		L;
		@Override
		public String getHelpMessage() {
			return "autolayout [on|off] ";
		}
	}
}
