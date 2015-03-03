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
package net.iubris.faci._di.guice.module.main;

import net.iubris.faci.analyzer.graphstream.AnalyzerModule;
import net.iubris.faci.console._di._guice.console.InteractiveConsoleModule;
import net.iubris.faci.grapher._di._guice.module.graphstream.GraphstreamModule;
import net.iubris.faci.parser._di._guice.module.ParserModule;
import net.iubris.faci.persister._di.guice.module.FaciPersisterModule;

import com.google.inject.AbstractModule;

public class FaciModule extends AbstractModule {

	@Override
	protected void configure() {
		install( new ParserModule());
		
		install( new InteractiveConsoleModule());
		
		install( new GraphstreamModule() );
		
		install( new AnalyzerModule() );
		
		install( new FaciPersisterModule() );
	}

}
