package net.iubris.facri._di.guice.module.main;

import net.iubris.facri._di.guice.module.console.InteractiveConsoleModule;
import net.iubris.facri._di.guice.module.parser.FacriParserModule;

import com.google.inject.AbstractModule;

public class FacriModule extends AbstractModule {

	@Override
	protected void configure() {
		install( new FacriParserModule());
		
		install( new InteractiveConsoleModule());
	}

}
