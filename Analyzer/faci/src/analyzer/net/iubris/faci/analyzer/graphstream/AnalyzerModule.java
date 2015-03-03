package net.iubris.faci.analyzer.graphstream;

import net.iubris.faci.analyzer.graphstream.GraphstreamAnalyzerSuffix.GraphstreamAnalyzerSuffixFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class AnalyzerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().build(GraphstreamAnalyzerSuffixFactory.class));
	}

}
