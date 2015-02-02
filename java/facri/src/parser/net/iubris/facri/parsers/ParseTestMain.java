/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ParseTestMain.java) is part of facri.
 * 
 *     ParseTestMain.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ParseTestMain.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.DataParser;

public class ParseTestMain {
	
	private final DataParser dataParser;
	
	@Inject
	public ParseTestMain(DataParser dataParser) {
		this.dataParser = dataParser;
	}

	private void parse() throws JAXBException, XMLStreamException, IOException {
		dataParser.parse();
		World world = dataParser.getResult();
		world.testData();
	}


	public static void main(String[] args) {
	
		Injector injector = Guice.createInjector( new FacriParserModule() );
		ParseTestMain debugMain = injector.getInstance(ParseTestMain.class);
		
		try {
			debugMain.parse();
		} catch (JAXBException | XMLStreamException | IOException e) {
			e.printStackTrace();
		}

	}

}
