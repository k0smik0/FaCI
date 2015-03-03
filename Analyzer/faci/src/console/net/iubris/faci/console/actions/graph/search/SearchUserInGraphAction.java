/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (SearchUserInGraphAction.java) is part of facri.
 * 
 *     SearchUserInGraphAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SearchUserInGraphAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.console.actions.graph.search;

import java.io.Console;
import java.util.Optional;

import javax.inject.Inject;

import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.utils.Printer;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Node;

public class SearchUserInGraphAction implements CommandAction {
	
	private final GraphsHolder graphHolder;
	private final World world;
	
	private static String WRONG = "wrong arguments";

	@Inject
	public SearchUserInGraphAction(GraphsHolder graphsHolder, World world) {
		this.graphHolder = graphsHolder;
		this.world = world;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		if (params==null || (params.length<1)) {
			console.printf(WRONG);
		} else {
			try {
				SearchArg cmd = SearchArg.valueOf(params[0]);
				switch (cmd) {
				case u:
					if (notEnoughArgs(params, console)) break;
					String userId = params[1];
					
					Optional<? extends User> userByUid = world.searchUserById(userId);
					if (userByUid.isPresent())
						doWhenUserFound( userByUid.get() );
					else
						Printer.println("no user found");
					
					break;
				case n:
					if (notEnoughArgs(params, console)) break;
					
					String fullName = "";
					for (int i=1; i<params.length;i++) {
						fullName += params[i]+" "; 
					}
					int lastIndexOf = fullName.lastIndexOf(" ");
					fullName = fullName.subSequence(0, lastIndexOf).toString();

					Optional<? extends User> userByName = world.searchUserByName(fullName);
					if (userByName.isPresent())
						doWhenUserFound( userByName.get() );
					else
						Printer.println("no user found");

					break;
				case m:
					doWhenUserFound( world.searchMe() );
					break;
				// default:
				// System.out.println( SearchUserInGraphCommand.S.getHelpMessage() );
				}
			} catch(IllegalArgumentException e) {
				Printer.println( SearchUserInGraphCommand.S.getHelpMessage() );
			}
		}
	}
	
	enum SearchArg {
		u, // user by id
		n, // user by name
		m; // me
	}
	
	private boolean notEnoughArgs(String[] params, Console console) {
		if (params.length < 2) {
			console.printf(WRONG);
			return true;
		}
		return false;
	}
	
	/*private void doWithOptional(Optional<User> optionalUser) {
		if (optionalUser.isPresent())
			doWhenUserFound( optionalUser.get() );
		else
			System.out.println("no user found");
	}*/
	
	private void doWhenUserFound(User user) {
//		markUser( graphHolder.getInteractionsGraph(), user.getUid() );
		System.out.println( user );
		markUser( user.getUid() );
//		return Optional.empty();
	}
	
	private void markUser(String userId) {
		if (graphHolder.isFriendshipsGraphCreated()) {
			Node node = graphHolder.getFriendshipsGraph().getNode(userId);
			actOnNode( node, userId );
		}
		if (graphHolder.isInteractionsGraphCreated()) {
			Node node = graphHolder.getInteractionsGraph().getNode(userId);
			actOnNode( node, userId );
		}
	}
	
	private void actOnNode(Node node, String userId) {
		node.addAttribute("ui.class", "marked");
		node.addAttribute("ui.label", userId);
		
		node.getAttributeKeySet().stream().forEach(ak->System.out.println(ak+" "+node.getAttribute(ak)));
	}
	
	public enum SearchUserInGraphCommand implements ConsoleCommand {
		S;
		private final String helpMessage = "search [m] | [u <user_id>] | [n <user_full_name>]\n\t\t[m = me; u = uid; n = name]";
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
	}

}
