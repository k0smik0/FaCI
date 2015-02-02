/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphCSSAction.java) is part of facri.
 * 
 *     GraphCSSAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphCSSAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.console.actions.graph.grapher;

import java.io.Console;

import javax.inject.Inject;

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class GraphCSSAction implements CommandAction {
	
//	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	private final GraphsHolder graphsHolder;
	
	@Inject
	public GraphCSSAction(
//			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator
			GraphsHolder graphsHolder) {
//		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
		this.graphsHolder = graphsHolder;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
//		graphstreamInteractionsGraphGenerator.reparseGraphCSS();
		graphsHolder.reparseCSS();
	}
	
	public enum GraphCSSCommand implements ConsoleCommand {
		C;
		@Override
		public String getHelpMessage() {
			return "reparse css for graph";
		}		
	}
}
