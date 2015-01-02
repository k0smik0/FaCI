package net.iubris.facri._di.guice.module.console;

import net.iubris.facri._di.providers.console.FacriInteractiveConsoleProvider;
import net.iubris.heimdall.InteractiveConsole;

import com.google.inject.AbstractModule;

public class InteractiveConsoleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InteractiveConsole.class).toProvider(FacriInteractiveConsoleProvider.class);
	}

}
