package net.iubris.facri._di.guice.module.console;

import net.iubris.facri._di.providers.console.FacriInteractiveConsoleProvider;
import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler.CacheHandlerFactory;
import net.iubris.heimdall.InteractiveConsole;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class InteractiveConsoleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InteractiveConsole.class).toProvider(FacriInteractiveConsoleProvider.class);
		
		install(new FactoryModuleBuilder().build(CacheHandlerFactory.class));
	}
}
