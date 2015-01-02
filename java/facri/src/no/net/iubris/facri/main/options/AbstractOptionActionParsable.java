package net.iubris.facri.main.options;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.parsers.DataParser;

public abstract class AbstractOptionActionParsable implements OptionAction {
	
	protected final DataParser dataParse;

	public AbstractOptionActionParsable(DataParser dataParse) {
		this.dataParse = dataParse;
	}
	
	protected void doParse() throws FileNotFoundException, JAXBException, XMLStreamException {
		dataParse.parse();
	}

}
