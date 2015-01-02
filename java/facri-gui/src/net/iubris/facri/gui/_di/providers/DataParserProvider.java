package net.iubris.facri.gui._di.providers;

import javax.inject.Provider;
import javax.inject.Singleton;

import com.google.inject.Guice;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.parsers.DataParser;

@Singleton
public class DataParserProvider implements Provider<DataParser> {

	private DataParser dataParser;

	@Override
	public DataParser get() {
		if (dataParser==null)
			dataParser = Guice.createInjector( new FacriParserModule() ).getInstance(DataParser.class);
		return dataParser;
	}

}
