/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (OptionArgument.java) is part of facri.
 * 
 *     OptionArgument.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     OptionArgument.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.main.options;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.module.parser.FacriParserModule;
import net.iubris.facri.parsers.DataParser;

import com.google.inject.Guice;
import com.google.inject.Injector;

public enum OptionArgument {
	
	
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
			
			OptionActionViewByGraphstream optionActionView = injector.getInstance(OptionActionViewByGraphstream.class);
			optionActionView.doGraph();
		}
	},
	HELP {
		@Override
		public void doCommand() {
			/*System.out.println("Facri 'argument'\n"
				+"arguments available are:\n"
				+"\tgraphml: generate graphml files"
				+"\tview: view graphs\n"
				+"\t[save | read] 'cacheFilename': use 'filename' to read from or save to parsed data, then visualize them");*/
			String helpString = "Available commands:\n"
					+"\t'e': exit\n"
					+"\t'h': display this help\n"
					+"\t'a': analyze [world] [analysis type]\n"
					+"\t\tanalyze command needs two arguments:\n"
					+"\t\tfirst argument select 'world' to analyze:"
					+"\t\t\t'f': analyze friendships 'world'\n"
					+"\t\t\t'i': analyze interactions 'world'\n"
					+"\t\tsecond argument select analysis type:\n"
					+"\t\t\t'mf': me and my friends\n"
					+"\t\t\t'ft': my friends and their friends (friends of friends)"
					+"\t\t\'tt': friends of my friends"
					+"\t\t\t'mft': me, my friends, their friends"
					+"\texample: 'a i mf': analyze interactions between me and my friends";
			System.out.println(helpString);
		}
	};
	
	public abstract void doCommand() throws Exception;
}
