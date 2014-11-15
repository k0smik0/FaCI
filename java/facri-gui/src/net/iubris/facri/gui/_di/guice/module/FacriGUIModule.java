package net.iubris.facri.gui._di.guice.module;

import net.iubris.facri._ishtaran.FacriCommandActionEventsHandlerProvider;
import net.iubris.ishtaran.gui._di.guice.modules.AbstractIshtaranModule;
import net.iubris.ishtaran.gui._di.providers.CommandActionEventsHandlerProvider;


public class FacriGUIModule extends AbstractIshtaranModule {

	@Override
	protected void configure() {
		super.configure();
	}

	@Override
	protected Class<? extends CommandActionEventsHandlerProvider> providesCommandActionEventsHandlerProviderType() {
		return FacriCommandActionEventsHandlerProvider.class;
	}

}
