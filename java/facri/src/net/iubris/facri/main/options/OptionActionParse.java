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
