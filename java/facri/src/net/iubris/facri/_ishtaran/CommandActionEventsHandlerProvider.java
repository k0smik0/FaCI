package net.iubris.facri._ishtaran;

import javax.inject.Inject;
import javax.inject.Provider;

import net.iubris.ishtaran.gui.actionevents.handler.CommandActionEventsHandler;

public class CommandActionEventsHandlerProvider implements Provider<CommandActionEventsHandler> {

	private CommandActionEventsHandler commandActionEventsHandler;

	@Inject
	public CommandActionEventsHandlerProvider(
			/*StemButtonCommandActionEvent stemButtonCommandActionEvent, 
			MultigraphsButtonCommandActionEvent multigraphsButtonCommandActionEvent,
			MiningButtonCommandActionEvent miningButtonCommandActionEvent*/
			
			) {
		/*commandActionEventsHandler = new CommandActionEventsHandler(
				stemButtonCommandActionEvent, 
				multigraphsButtonCommandActionEvent,
				miningButtonCommandActionEvent
				);*/
	}
	
	@Override
	public CommandActionEventsHandler get() {
		return commandActionEventsHandler;
	}
}
