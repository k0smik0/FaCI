package net.iubris.facri.main;

import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.google.inject.Guice;
import com.google.inject.Injector;

import net.iubris.facri._di.guice.module.parser.FacriModule;
import net.iubris.facri.parsers.DataParser;

public class DebugMain {
	
	private final DataParser dataParser;
	
	@Inject
	public DebugMain(DataParser dataParser) {
		this.dataParser = dataParser;
	}

	private void parse() throws JAXBException, XMLStreamException, IOException {
		dataParser.parse();
	}


	public static void main(String[] args) {
	
		Injector injector = Guice.createInjector( new FacriModule() );
		DebugMain debugMain = injector.getInstance(DebugMain.class);
		
		try {
			debugMain.parse();
		} catch (JAXBException | XMLStreamException | IOException e) {
			e.printStackTrace();
		}

	}

}
