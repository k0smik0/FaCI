/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriInteractiveConsoleProvider.java) is part of facri.
 * 
 *     FacriInteractiveConsoleProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriInteractiveConsoleProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.console._di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.faci.console.actions.graph.analyzer.AnalyzeAction;
import net.iubris.faci.console.actions.graph.grapher.GraphCSSAction;
import net.iubris.faci.console.actions.graph.grapher.GrapherAction;
import net.iubris.faci.console.actions.graph.layout.GraphLayoutAction;
import net.iubris.faci.console.actions.graph.nozerodegree.ClearUselessNodesAction;
import net.iubris.faci.console.actions.graph.search.SearchUserInGraphAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FaciInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FaciInteractiveConsoleProvider( 
			GrapherAction grapherAction, 
			GraphCSSAction graphCSSAction, 
			GraphLayoutAction graphLayoutAction, 
			SearchUserInGraphAction searchUserInGraphAction, 
			AnalyzeAction analyzeAction,
			ClearUselessNodesAction clearUselessNodesAction
			) {
		
		this.interactiveConsole = new InteractiveConsole(
				"Faci",
				new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction),
				new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction),
				new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction),
				new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction),
				new CommandWithAction(ClearUselessNodesAction.ClearUselessNodesActionCommand.Z, clearUselessNodesAction)
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
