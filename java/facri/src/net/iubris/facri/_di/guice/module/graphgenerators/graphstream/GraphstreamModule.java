package net.iubris.facri._di.guice.module.graphgenerators.graphstream;

import net.iubris.facri.graph.generators.graphstream.GraphstreamInteractionsGraphGenerator;

import com.google.inject.AbstractModule;

public class GraphstreamModule extends AbstractModule {

	@Override
	protected void configure() {
		requestStaticInjection(GraphstreamInteractionsGraphGenerator.class);
	}

}
