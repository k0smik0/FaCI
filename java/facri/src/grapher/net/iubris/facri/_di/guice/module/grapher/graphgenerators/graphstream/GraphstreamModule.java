package net.iubris.facri._di.guice.module.grapher.graphgenerators.graphstream;

import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler.CacheHandlerFactory;
import net.iubris.facri.grapher.analyzer.graphstream.GraphstreamAnalyzer.GraphstreamAnalyzerFactory;
import net.iubris.facri.model.graph.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.facri.model.graph.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GraphstreamModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().build(CacheHandlerFactory.class));
		install(new FactoryModuleBuilder().build(FriendshipsMouseManagerFactory.class));
		install(new FactoryModuleBuilder().build(InteractionsMouseManagerFactory.class));
		install(new FactoryModuleBuilder().build(GraphstreamAnalyzerFactory.class)); 
	}
}
