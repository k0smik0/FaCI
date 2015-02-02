/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (OptionActionParse.java) is part of facri.
 * 
 *     OptionActionParse.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     OptionActionParse.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.main.options;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.parsers.DataParser;

public class OptionActionParse implements OptionAction {
	
	private final DataParser dataParse;

	@Inject
	public OptionActionParse(DataParser dataParse) {
		this.dataParse = dataParse;
	}
	
//	protected void doParse() throws FileNotFoundException, JAXBException, XMLStreamException {
//		dataParse.parse();
//	}

	@Override
	public void execute() throws FileNotFoundException, JAXBException, XMLStreamException {
		dataParse.parse();
	}

}
