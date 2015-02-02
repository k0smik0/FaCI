/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (InteractiveConsoleModule.java) is part of facri.
 * 
 *     InteractiveConsoleModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     InteractiveConsoleModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.guice.module.console;

import net.iubris.facri._di.providers.console.FacriInteractiveConsoleProvider;
import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler.CacheHandlerFactory;
import net.iubris.heimdall.InteractiveConsole;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class InteractiveConsoleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InteractiveConsole.class).toProvider(FacriInteractiveConsoleProvider.class);
		
		install(new FactoryModuleBuilder().build(CacheHandlerFactory.class));
	}
}
