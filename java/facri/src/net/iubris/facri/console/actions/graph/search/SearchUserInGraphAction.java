package net.iubris.facri.console.actions.graph.search;

import java.io.Console;

import javax.inject.Inject;

import net.iubris.facri.model.World;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.users.User;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class SearchUserInGraphAction implements CommandAction {
	
	private final GraphsHolder graphHolder;
	private final World world;

	@Inject
	public SearchUserInGraphAction(GraphsHolder graphHolder, World world) {
		this.graphHolder = graphHolder;
		this.world = world;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		// TODO Auto-generated method stub
		if (params==null || (params!=null && params.length<1)) {
			console.printf("wrong arguments");
			return;
		}
		
		String cmd = params[0];
		switch (cmd) {
		case "uid":
			String userId = params[1];
			doWhenUserFound( world.searchUserById(userId).get() );
			break;
		case "name":
			String name = params[1];
			doWhenUserFound( world.searchUserByName(name).get() );
			break;
		case "me":
			doWhenUserFound( world.searchMe() );
			break;
		default:
			System.out.println( SearchUserInGraphCommand.S.getHelpMessage() );
		}

//		100004559771088
	}
	
	private void doWhenUserFound(User user) {
//		markUser( graphHolder.getInteractionsGraph(), user.getUid() );
		markUser( user.getUid() );
		System.out.println( user );
	}
	
	/*private void markUser(Graph graph, String userId) {
		if (graph!=null) {
//			graph.getNode(userId).addAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
//			graph.getNode(userId).setAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
			graph.getNode(userId).setAttribute("ui.class","marked");
		}
	}*/
	private void markUser(String userId) {
		graphHolder.getFriendshipsGraph().getNode(userId).setAttribute("ui.class","marked");
		graphHolder.getInteractionsGraph().getNode(userId).setAttribute("ui.class","marked");
	}
	
	public enum SearchUserInGraphCommand implements ConsoleCommand {
		S;
		@Override
		public String getHelpMessage() {
			return "search [me] | [uid <user_id>] | [name <user_full_name>] ";
		}
	}

}
