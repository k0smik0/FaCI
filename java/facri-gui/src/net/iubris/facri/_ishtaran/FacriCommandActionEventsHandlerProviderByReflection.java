package net.iubris.facri._ishtaran;

import javax.inject.Inject;

import sun.reflect.Reflection;
import net.iubris.facri.gui.actionevents.parse.ParseButtonCommandActionEvent;
import net.iubris.ishtaran.gui._di.providers.AbstractCommandActionEventsHandlerProvider;
import net.iubris.ishtaran.gui._di.providers.CommandActionEventsHandlerProvider;
import net.iubris.ishtaran.gui.actionevents.handler.CommandActionEventsHandler;

public class FacriCommandActionEventsHandlerProviderByReflection implements CommandActionEventsHandlerProvider {


	private CommandActionEventsHandler commandActionEventsHandler;

//	@Inject
//	public FacriCommandActionEventsHandlerProviderByReflection(
//			/*StemButtonCommandActionEvent stemButtonCommandActionEvent, 
//			MultigraphsButtonCommandActionEvent multigraphsButtonCommandActionEvent,
//			MiningButtonCommandActionEvent miningButtonCommandActionEvent*/
//			ParseButtonCommandActionEvent parseButtonCommandActionEvent		
//			) {
//		super(parseButtonCommandActionEvent);
//	}

	@Override
	public CommandActionEventsHandler get() {
//		if (commandActionEventsHandler==null) {
//			Class.forName(className)
//		}
		return null;
	}
	
}
