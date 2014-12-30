package net.iubris.facri.console.actions.graph.grapher;

import java.io.Console;

import javax.inject.Inject;

import net.iubris.facri.graph.generators.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class GraphCSSAction implements CommandAction {
	
	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	@Inject
	public GraphCSSAction(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		graphstreamInteractionsGraphGenerator.reparseGraphCSS();
	}
	
	public enum GraphCSSCommand implements ConsoleCommand {
		C;
		@Override
		public String getHelpMessage() {
			return "reparse css for graph";
		}		
	}

}
