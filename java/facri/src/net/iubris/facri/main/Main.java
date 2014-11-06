package net.iubris.facri.main;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.FacriModule;
import net.iubris.facri.parsers.GlobalParser;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class Main {

	public static void main(String[] args) {
		
		Injector injector = Guice.createInjector( new FacriModule() );
		
		GlobalParser jsonParser = injector.getInstance(GlobalParser.class);
		try {
			jsonParser.parse();
		} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
