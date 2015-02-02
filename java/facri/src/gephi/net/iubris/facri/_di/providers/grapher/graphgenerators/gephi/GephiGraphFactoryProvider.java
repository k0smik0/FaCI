/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GephiGraphFactoryProvider.java) is part of facri.
 * 
 *     GephiGraphFactoryProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GephiGraphFactoryProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.providers.grapher.graphgenerators.gephi;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;

@Singleton
public class GephiGraphFactoryProvider implements Provider<GraphFactory> {
	
	private final GraphFactory graphFactory;

	@Inject
	public GephiGraphFactoryProvider(GraphModel graphModel) {
		graphFactory = graphModel.factory();
	}
	
	@Override
	public GraphFactory get() {
		return graphFactory;
	}

}
