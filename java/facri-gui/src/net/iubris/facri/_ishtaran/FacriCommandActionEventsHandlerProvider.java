package net.iubris.facri._ishtaran;

import javax.inject.Inject;

import net.iubris.facri.gui.actionevents.ParseButtonCommandActionEvent;
import net.iubris.ishtaran.gui._di.providers.AbstractCommandActionEventsHandlerProvider;

public class FacriCommandActionEventsHandlerProvider extends AbstractCommandActionEventsHandlerProvider {


	@Inject
	public FacriCommandActionEventsHandlerProvider(
			/*StemButtonCommandActionEvent stemButtonCommandActionEvent, 
			MultigraphsButtonCommandActionEvent multigraphsButtonCommandActionEvent,
			MiningButtonCommandActionEvent miningButtonCommandActionEvent*/
			ParseButtonCommandActionEvent parseButtonCommandActionEvent		
			) {
		super(parseButtonCommandActionEvent);
	}
	
}
