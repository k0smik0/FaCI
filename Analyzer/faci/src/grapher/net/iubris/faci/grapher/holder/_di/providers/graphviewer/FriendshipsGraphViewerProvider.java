package net.iubris.faci.grapher.holder._di.providers.graphviewer;

import javax.inject.Inject;

import net.iubris.faci.grapher.holder._di.annotations.graph.GraphFriendships;

import org.graphstream.graph.Graph;

public class FriendshipsGraphViewerProvider extends AbstractGraphViewerProvider {

	@Inject
	public FriendshipsGraphViewerProvider(@GraphFriendships Graph graph) {
		super(graph);
	}
}
