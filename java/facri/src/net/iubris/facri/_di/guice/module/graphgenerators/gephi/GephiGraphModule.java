package net.iubris.facri._di.guice.module.graphgenerators.gephi;

import net.iubris.facri._di.providers.graphgenerators.gephi.GephiDirectedGraphProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiGraphFactoryProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiGraphModelProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiUndirectedGraphProvider;
import net.iubris.facri._di.providers.graphgenerators.gephi.GephiWorkspaceProvider;
import net.iubris.facri.graph.exporter.GraphExporter;
import net.iubris.facri.graph.exporter.gephi.GephiGraphExporter;
import net.iubris.facri.graph.generator.FriendshipsGraphGenerator;
import net.iubris.facri.graph.generator.InteractionsGraphGenerator;
import net.iubris.facri.graph.generator.gephi.GephiFriendshipsGraphGenerator;
import net.iubris.facri.graph.generator.gephi.GephiInteractionsGraphGenerator;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.project.api.Workspace;

import com.google.inject.AbstractModule;

public class GephiGraphModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Workspace.class).toProvider(GephiWorkspaceProvider.class);
		bind(GraphModel.class).toProvider(GephiGraphModelProvider.class);
		bind(GraphFactory.class).toProvider(GephiGraphFactoryProvider.class);
		bind(DirectedGraph.class).toProvider(GephiDirectedGraphProvider.class);
		bind(UndirectedGraph.class).toProvider(GephiUndirectedGraphProvider.class);
		
		bind(GraphExporter.class).to(GephiGraphExporter.class);
		bind(FriendshipsGraphGenerator.class).to(GephiFriendshipsGraphGenerator.class);
		bind(InteractionsGraphGenerator.class).to(GephiInteractionsGraphGenerator.class);
	}

}
