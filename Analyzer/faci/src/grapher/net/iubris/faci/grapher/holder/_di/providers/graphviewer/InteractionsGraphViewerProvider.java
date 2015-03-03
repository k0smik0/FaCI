package net.iubris.faci.grapher.holder._di.providers.graphviewer;

import javax.inject.Inject;

import net.iubris.faci.grapher.holder._di.annotations.graph.GraphInteractions;

import org.graphstream.graph.Graph;

public class InteractionsGraphViewerProvider extends AbstractGraphViewerProvider {

	@Inject
	public InteractionsGraphViewerProvider(@GraphInteractions Graph graph) {
		super(graph);
	}
}