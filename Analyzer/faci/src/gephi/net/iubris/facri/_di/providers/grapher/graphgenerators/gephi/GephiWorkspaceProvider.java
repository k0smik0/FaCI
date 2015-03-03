/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GephiWorkspaceProvider.java) is part of facri.
 * 
 *     GephiWorkspaceProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GephiWorkspaceProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.providers.grapher.graphgenerators.gephi;

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
