package net.iubris.facri._di.guice.module.grapher.graphgenerators.graphstream;

import net.iubris.facri._di.guice.grapher.factories.CacheHandlerFactory;
import net.iubris.facri.model.graph.eventmanagers.InternalMouseManager.InternalMouseManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GraphstreamModule extends AbstractModule {

	@Override
	protected void configure() {
//		requestStaticInjection(GraphstreamInteractionsGraphGenerator.class);
		install(new FactoryModuleBuilder().build(CacheHandlerFactory.class));
		install(new FactoryModuleBuilder().build(InternalMouseManagerFactory.class));
	}
}
