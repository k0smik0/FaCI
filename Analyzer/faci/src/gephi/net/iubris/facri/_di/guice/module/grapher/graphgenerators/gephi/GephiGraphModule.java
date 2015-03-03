/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GephiGraphModule.java) is part of facri.
 * 
 *     GephiGraphModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GephiGraphModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.guice.module.grapher.graphgenerators.gephi;

import net.iubris.facri._di.providers.grapher.graphgenerators.gephi.GephiDirectedGraphProvider;
import net.iubris.facri._di.providers.grapher.graphgenerators.gephi.GephiGraphFactoryProvider;
import net.iubris.facri._di.providers.grapher.graphgenerators.gephi.GephiGraphModelProvider;
import net.iubris.facri._di.providers.grapher.graphgenerators.gephi.GephiUndirectedGraphProvider;
import net.iubris.facri._di.providers.grapher.graphgenerators.gephi.GephiWorkspaceProvider;

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
		
//		bind(GraphExporter.class).to(GephiGraphExporter.class);
//		bind(FriendshipsGraphGenerator.class).to(GephiFriendshipsGraphGenerator.class);
//		bind(InteractionsGraphGenerator.class).to(GephiInteractionsGraphGenerator.class);
	}

}
