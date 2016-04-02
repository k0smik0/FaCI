/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CacheHandler.java) is part of facri.
 * 
 *     CacheHandler.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CacheHandler.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.cache;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.cache.persister.WorldPersisterService;
import net.iubris.faci.console.actions.graph.grapher.GrapherExecutor.GraphGenerationDoneFunction;
import net.iubris.faci.console.actions.graph.grapher.GrapherExecutor.GraphGenerationFunction;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.parser.parsers.GlobalParser;
import net.iubris.faci.utils.Pauser;
import net.iubris.faci.utils.Printer;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceGraphML;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class CacheHandler {
	
	private boolean readFromCache;
	private boolean write;
	private boolean writeOnlyGraph;
	
	private final GlobalParser dataParser;
	
	private final String dirTree;
	
	private String cacheType;
	private FileSink fileSink;
	private String fileExtension;
	
	private final WorldPersisterService worldPersisterService;
	private final String myUserId;
	
	
	@AssistedInject
	public CacheHandler(
			@Named("cache_type") String cacheType,
			@Named("my_user_id") String myUserId,
			GlobalParser dataParser
			, WorldPersisterService worldPersisterService
			, @Assisted String[] params
			) {
		this.cacheType = cacheType;
		this.myUserId = myUserId;
//		this.myUserId = myUserId;
//		this.corpusPrefix = meId;
		this.worldPersisterService = worldPersisterService;
		fileExtension = cacheType.toLowerCase();
		this.dataParser = dataParser;
		this.dirTree = "cache"+File.separatorChar+"graphs"+File.separatorChar+myUserId;
		handlesParams(params);
//		if (!readFromCache || !write) {
//			// parse
//		}
	}
	
	private void handlesParams(String[] actionParams) {
		if (actionParams.length>2) {
			String useCacheString = actionParams[2];		
			UseCacheArguments useCacheArguments = UseCacheArguments.valueOf(useCacheString);
//			System.out.println(useCacheArguments.name());
//			try {
				switch(useCacheArguments) {
					// if read from cache, do not parse
					case cr:
						readFromCache = true;
						break;
					// we want write cache, so parse for new data
					case cw:
						write = true;
						break;
					case cwg: 
						writeOnlyGraph = true;
						break;
					default:
						Printer.println("wrong cache argument");
						break;
				}
//			} catch (IllegalArgumentException e) {
//				Printer.println("wrong cache argument");
//			}
		}
	}
	
	public void exec(
			Graph graph, 
			String fileBasename,
			GraphGenerationFunction graphGeneratorFunction,
			GraphGenerationDoneFunction graphGeneratorDoneFunction 
			) throws IOException, JAXBException, XMLStreamException {
		String filename = myUserId+"_-_"+fileBasename;
		graph.addAttribute(GraphsHolder.graph_file_name, filename);
		readIfPresent(graph,
				filename,
				graphGeneratorFunction,
				graphGeneratorDoneFunction)
		.writeIfWanted(graph, filename);
	}
	
	public CacheHandler readIfPresent(Graph graph, String cacheFilename, GraphGenerationFunction graphGeneratorFunction,
			GraphGenerationDoneFunction graphGeneratorDoneFunction) throws IOException, JAXBException, XMLStreamException {
		if (readFromCache) {
			// read parsed data
			worldPersisterService.populate();
			// read graphs
			readGraph(graph, cacheFilename+"."+getCacheFileExtension());
		} else {
			dataParser.parse();
			graphGeneratorFunction.generate();
		}
		graphGeneratorDoneFunction.setGenerated();
		return this;
	}	
	private void readGraph(Graph graph, String filename) throws IOException {
//		System.out.println(graph.getAttribute(GrapherExecutor.graph_name));
		String filenamePath = dirTree+File.separatorChar+filename;
		FileSource fileSource =  
//					FileSourceFactory.sourceFor(filenamePath);
				new FileSourceGraphML();
		fileSource.addSink(graph);
//			fileSource.readAll(path); // read one-shot
		try {
			fileSource.begin(filenamePath);
			while (fileSource.nextEvents()) {
				// Optionally some code here ...
				Pauser.sleep(5);
			}
			fileSource.end();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileSource.removeSink(graph);
		}
	}
	
	public void writeIfWanted(Graph graph, String filenameBasename) throws IOException {
		if (writeOnlyGraph ) { 
			// write graphs
			writeGraphs(graph, filenameBasename);
			return;
		}
		if (write) {
			// write parsed data
			worldPersisterService.persist();
		}
	}
	private void writeGraphs(Graph graph, String filenameBasename) throws IOException {
		prepareSink();
		File cacheDir = new File(dirTree);
		if (!cacheDir.exists())
			cacheDir.mkdir();
		fileSink.writeAll(graph, dirTree+File.separatorChar+filenameBasename+"."+fileExtension);
	}	
	private void prepareSink() {
		if (fileSink==null) {
			try {
				fileSink = (FileSink) //				new FileSinkGraphML();
						Class.forName("FileSink"+cacheType).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//				e.printStackTrace();
				// TODO: refactor
				// fallback to graphml 
				fileSink = new FileSinkGraphML();
				//					new FileSinkDGS();
				cacheType = "GraphML";
				fileExtension = "graphml";
			}
		}
	}
	
	public String getCacheFileExtension() {
		return fileExtension; //"graphml";
	}
	
	public enum UseCacheArguments implements ConsoleCommand {
		cr("cache read: (try to) import previous generated graph from a cached file"),
		cw("cache write: save parsed data on disk, then export generated graphs in a GraphStream compliant type file"),
		cwg("cache write export generated graphs in a GraphStream compliant type file");
		UseCacheArguments(String helpMessageCore) {
			this.helpMessage = getPrefix(this,3)+helpMessageCore+"\n";
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
	
	public interface CacheHandlerFactory {
		CacheHandler create(String[] params);
	}
}
