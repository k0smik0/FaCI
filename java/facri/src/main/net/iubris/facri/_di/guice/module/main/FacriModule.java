/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriModule.java) is part of facri.
 * 
 *     FacriModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.guice.module.main;

import net.iubris.facri._di.guice.module.console.InteractiveConsoleModule;
import net.iubris.facri._di.guice.module.grapher.graphgenerators.graphstream.GraphstreamModule;
import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.persister._di.guice.module.FacriPersisterModule;

import com.google.inject.AbstractModule;

public class FacriModule extends AbstractModule {

	@Override
	protected void configure() {
		install( new FacriParserModule());
		
		install( new InteractiveConsoleModule());
		
		install( new GraphstreamModule() );
		
		install( new FacriPersisterModule() );
	}

}
