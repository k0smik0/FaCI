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
