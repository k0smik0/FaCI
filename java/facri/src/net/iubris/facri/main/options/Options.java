package net.iubris.facri.main.options;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.parsers.DataParser;

import com.google.inject.Guice;
import com.google.inject.Injector;

public enum Options {
	
	
	PARSE {
		@Override
		public void doCommand() throws FileNotFoundException, JAXBException, XMLStreamException {
			DataParser dataParser = Guice.createInjector( new FacriParserModule() ).getInstance(DataParser.class);
			dataParser.parse();
		}
	},
	VIEW {
		@Override
		public void doCommand() throws FileNotFoundException, JAXBException, XMLStreamException {
			Injector injector = Guice.createInjector( new FacriParserModule() );
			DataParser dataParser = injector.getInstance(DataParser.class);
			dataParser.parse();
			
//			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator = injector.getInstance(GraphstreamInteractionsGraphGenerator.class);
//			graphstreamInteractionsGraphGenerator.
			
			OptionActionView optionActionView = injector.getInstance(OptionActionView.class);
			optionActionView.doGraph();
		}
	};
	
	public abstract void doCommand() throws Exception;
}
