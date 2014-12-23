package net.iubris.facri.console.actions.graph;

import java.io.Console;

import javax.inject.Inject;

import org.graphstream.graph.Graph;

import net.iubris.facri.model.graph.GraphHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class SearchUserInGraphAction implements CommandAction {
	
	private final GraphHolder graphHolder;

	@Inject
	public SearchUserInGraphAction(GraphHolder graphHolder) {
		this.graphHolder = graphHolder;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		// TODO Auto-generated method stub
		if (params==null || (params!=null && params.length<1)) {
			console.printf("wrong arguments");
			return;
		}
		
		String userId = params[0];

//		markUser(graphHolder.getFriendshipsGraph(), userId);
		markUser(graphHolder.getInteractionsGraph(), userId);
		
//		100004559771088
	}
	
	private void markUser(Graph graph, String userId) {
		if (graph!=null) {
//			graph.getNode(userId).addAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
//			graph.getNode(userId).setAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
			graph.getNode(userId).setAttribute("ui.class","marked");
		}
	}
	
	public enum SearchUserInGraphCommand implements ConsoleCommand {
		S;

		@Override
		public String getHelpMessage() {
			return "search [user_id]";
		}
		
	}

}
