package net.iubris.faci.grapher.holder._di.providers.graphviewer;

import javax.inject.Provider;

import net.iubris.faci.grapher.holder.core.ViewUtils;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

public abstract class AbstractGraphViewerProvider implements Provider<Viewer> {

	private final Viewer viewer;

	public AbstractGraphViewerProvider(Graph graph) {
		this.viewer = ViewUtils.buildViewer(graph);
	}

	@Override
	public Viewer get() {
		return viewer;
	}
}
