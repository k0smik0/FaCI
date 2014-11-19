package net.iubris.facri._di.guice.module.graphgenerators.gephi;

import net.iubris.facri._di.providers.graphgenerators.gephi.GephiDirectedGraphProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiGraphFactoryProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiGraphModelProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiWorkspaceProvider;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;

import com.google.inject.AbstractModule;

public class GephiGraphGeneratorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Workspace.class).toProvider(GephiWorkspaceProvider.class);
		bind(GraphFactory.class).toProvider(GephiGraphFactoryProvider.class);
		bind(GraphModel.class).toProvider(GephiGraphModelProvider.class);
		bind(DirectedGraph.class).toProvider(GephiDirectedGraphProvider.class);
	}

}
