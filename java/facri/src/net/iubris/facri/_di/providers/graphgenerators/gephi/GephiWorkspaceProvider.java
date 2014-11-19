package net.iubris.facri._di.providers.graphgenerators.gephi;

import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

@Singleton
public class GephiWorkspaceProvider implements Provider<Workspace> {
	
	private final Workspace workspace;

	public GephiWorkspaceProvider() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
      pc.newProject();
      workspace = pc.getCurrentWorkspace();
	}

	@Override
	public Workspace get() {
		return workspace;
	}
}
