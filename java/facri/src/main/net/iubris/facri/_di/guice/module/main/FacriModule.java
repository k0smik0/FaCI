package net.iubris.facri._di.guice.module.main;

import net.iubris.facri._di.guice.module.console.InteractiveConsoleModule;
import net.iubris.facri._di.guice.module.grapher.graphgenerators.graphstream.GraphstreamModule;
import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.persisters._di.guice.module.FacriPersisterModule;

import com.google.inject.AbstractModule;

public class FacriModule extends AbstractModule {

	@Override
	protected void configure() {
		install( new FacriParserModule());
		
		install( new InteractiveConsoleModule());
		
		install( new GraphstreamModule() );
		
		install( new FacriPersisterModule() );
	}

}
