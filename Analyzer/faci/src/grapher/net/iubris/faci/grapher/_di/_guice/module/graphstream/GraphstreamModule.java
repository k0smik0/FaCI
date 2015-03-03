/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphstreamModule.java) is part of facri.
 * 
 *     GraphstreamModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphstreamModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher._di._guice.module.graphstream;

import net.iubris.faci.grapher.holder._di._guice.module.ModelModule;
import net.iubris.faci.grapher.holder.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.faci.grapher.holder.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class GraphstreamModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ModelModule());
		
		install(new FactoryModuleBuilder().build(FriendshipsMouseManagerFactory.class));
		install(new FactoryModuleBuilder().build(InteractionsMouseManagerFactory.class));
	}
}
