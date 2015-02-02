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
package net.iubris.facri._di.providers.console;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.facri.console.actions.graph.analyzer.AnalyzeAction;
import net.iubris.facri.console.actions.graph.grapher.GraphCSSAction;
import net.iubris.facri.console.actions.graph.grapher.GrapherAction;
import net.iubris.facri.console.actions.graph.layout.GraphLayoutAction;
import net.iubris.facri.console.actions.graph.nozerodegree.ClearUselessNodesAction;
import net.iubris.facri.console.actions.graph.search.SearchUserInGraphAction;
import net.iubris.heimdall.CommandWithAction;
import net.iubris.heimdall.InteractiveConsole;

public class FacriInteractiveConsoleProvider implements Provider<InteractiveConsole> {

	private final InteractiveConsole interactiveConsole;

	@Inject
	public FacriInteractiveConsoleProvider( 
			GrapherAction grapherAction, 
			GraphCSSAction graphCSSAction, 
			GraphLayoutAction graphLayoutAction, 
			SearchUserInGraphAction searchUserInGraphAction, 
			AnalyzeAction analyzeAction,
			ClearUselessNodesAction clearUselessNodesAction
//			, HelpersHolder helpersHolder
			) {
		
//		Set<CommandWithAction> cwa = new HashSet<>();
//		cwa.add(new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction));
//		cwa.add(new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction));
//		cwa.add(new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction));
//		cwa.add(new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction));
//		cwa.add(new CommandWithAction(ClearUselessNodesAction.ClearUselessNodesActionCommand.Z, clearUselessNodesAction));
//		new CommandWithAction(GraphLayoutAction.GraphLayoutCommand.L, graphLayoutAction),
		
//		Set<PreExit> preExits = new HashSet<>();
//		System.out.println( helpersHolder.getHelpers().size() );

//		helpersHolder.getHelpers().stream().forEach(h-> {
//			preExits.add( h::closeStorage);
//			System.out.println("h::closeStorage");
//		});
		
		this.interactiveConsole = new InteractiveConsole(
				"Facri",
				
				new CommandWithAction(GrapherAction.GrapherCommand.G, grapherAction),
				new CommandWithAction(AnalyzeAction.AnalyzeCommand.A, analyzeAction),
				new CommandWithAction(SearchUserInGraphAction.SearchUserInGraphCommand.S, searchUserInGraphAction),
				new CommandWithAction(GraphCSSAction.GraphCSSCommand.C, graphCSSAction),
				new CommandWithAction(ClearUselessNodesAction.ClearUselessNodesActionCommand.Z, clearUselessNodesAction)
				
//				cwa
//				preExits
				);
	}
	
	@Override
	public InteractiveConsole get() {
		return interactiveConsole;
	}

}
