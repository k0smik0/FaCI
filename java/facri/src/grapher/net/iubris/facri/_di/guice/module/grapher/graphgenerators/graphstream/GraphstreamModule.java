package net.iubris.facri._di.guice.module.grapher.graphgenerators.graphstream;

import net.iubris.facri.grapher.generators.interactions.graphstream.GraphstreamInteractionsGraphGenerator;

import com.google.inject.AbstractModule;

public class GraphstreamModule extends AbstractModule {

	@Override
	protected void configure() {
		requestStaticInjection(GraphstreamInteractionsGraphGenerator.class);
	}

}
