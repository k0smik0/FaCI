/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (TestParsing.java) is part of facri.
 * 
 *     TestParsing.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     TestParsing.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.test_main;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.parsers.DataParser;

public class TestParsing {
	
	private final DataParser dataParser;
	
	@Inject
	public TestParsing(DataParser dataParser) {
		this.dataParser = dataParser;
	}

	private void parse() throws JAXBException, XMLStreamException, IOException {
		dataParser.parse();
	}


	public static void main(String[] args) {
	
		Injector injector = Guice.createInjector( new FacriParserModule() );
		TestParsing debugMain = injector.getInstance(TestParsing.class);
		
		try {
			debugMain.parse();
		} catch (JAXBException | XMLStreamException | IOException e) {
			e.printStackTrace();
		}

	}

}
