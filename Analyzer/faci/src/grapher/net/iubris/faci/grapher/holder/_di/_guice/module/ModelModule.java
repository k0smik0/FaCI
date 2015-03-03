package net.iubris.faci.grapher.holder._di._guice.module;

import net.iubris.faci.grapher.holder._di.annotations.graph.GraphFriendships;
import net.iubris.faci.grapher.holder._di.annotations.graph.GraphInteractions;
import net.iubris.faci.grapher.holder._di.annotations.graphviewer.FriendshipsGraphViewer;
import net.iubris.faci.grapher.holder._di.annotations.graphviewer.InteractionsGraphViewer;
import net.iubris.faci.grapher.holder._di.providers.graph.FriendshipsGraphProvider;
import net.iubris.faci.grapher.holder._di.providers.graph.InteractionsGraphProvider;
import net.iubris.faci.grapher.holder._di.providers.graphviewer.FriendshipsGraphViewerProvider;
import net.iubris.faci.grapher.holder._di.providers.graphviewer.InteractionsGraphViewerProvider;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

import com.google.inject.AbstractModule;

public class ModelModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Graph.class).annotatedWith(GraphFriendships.class).toProvider(FriendshipsGraphProvider.class);
		bind(Graph.class).annotatedWith(GraphInteractions.class).toProvider(InteractionsGraphProvider.class);
		bind(Viewer.class).annotatedWith(FriendshipsGraphViewer.class).toProvider(FriendshipsGraphViewerProvider.class);
		bind(Viewer.class).annotatedWith(InteractionsGraphViewer.class).toProvider(InteractionsGraphViewerProvider.class);
	}
}
