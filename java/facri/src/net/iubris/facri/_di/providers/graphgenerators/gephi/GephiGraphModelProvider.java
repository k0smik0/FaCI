package net.iubris.facri._di.providers.graphgenerators.gephi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

@Singleton
public class GephiGraphModelProvider implements Provider<GraphModel> {
	
	private final GraphModel graphModel;

	@Inject
	public GephiGraphModelProvider(Workspace workspace) {
		graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	}

	@Override
	public GraphModel get() {
		return graphModel;
	}
}
