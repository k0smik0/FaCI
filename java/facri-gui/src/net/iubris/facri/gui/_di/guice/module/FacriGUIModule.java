package net.iubris.facri.gui._di.guice.module;

import net.iubris.facri._ishtaran.FacriCommandActionEventsHandlerProvider;
import net.iubris.ishtaran.gui._di.guice.modules.AbstractIshtaranModule;
import net.iubris.ishtaran.gui.actionevents.handler.CommandActionEventsHandler;


public class FacriGUIModule extends AbstractIshtaranModule {

	@Override
	protected void configure() {
		super.configure();
	}

	@Override
	protected void bindCommandActionEventsHandlerToProvider() {
		bind(CommandActionEventsHandler.class).toProvider(FacriCommandActionEventsHandlerProvider.class);
	}

}
