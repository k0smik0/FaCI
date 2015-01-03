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
