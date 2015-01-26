package net.iubris.facri.console.actions.graph.utils.cache;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphGenerationDoneFunction;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphGenerationFunction;
import net.iubris.facri.console.actions.graph.utils.cache.persister.WorldPersisterService;
import net.iubris.facri.parsers.DataParser;
import net.iubris.facri.utils.Pauser;
import net.iubris.facri.utils.Printer;
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
	
	private final DataParser dataParser;
	
	private final String dirTree;
	
	private String cacheType;
	private FileSink fileSink;
	private String fileExtension;
	
	private final WorldPersisterService worldPersisterService;
	
	@AssistedInject
	public CacheHandler(
			@Named("cache_type") String cacheType,
			@Named("my_user_id") String myUserId,
			DataParser dataParser
			, WorldPersisterService worldPersisterService
			, @Assisted String[] params
			) {
		this.cacheType = cacheType;
//		this.myUserId = myUserId;
//		this.corpusPrefix = meId;
		this.worldPersisterService = worldPersisterService;
		this.fileExtension = cacheType.toLowerCase();
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
		readIfPresent(graph,
				fileBasename,
				graphGeneratorFunction,
				graphGeneratorDoneFunction)
		.writeIfWanted(graph, fileBasename);
	}
	
	public CacheHandler readIfPresent(Graph graph, String cacheFilename, GraphGenerationFunction graphGeneratorFunction,
			GraphGenerationDoneFunction graphGeneratorDoneFunction) throws IOException, JAXBException, XMLStreamException {
		if (readFromCache) {
//			 TODO persister: restore populate()
			worldPersisterService.populate();
//			String cacheFilename = graph
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
		if (write) {
			// TODO persister: restore persist()
			worldPersisterService.persist();
			// write graphs
			if (fileSink==null) {
				try {
					fileSink = (FileSink) //				new FileSinkGraphML();
							Class.forName("FileSink"+cacheType).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//					e.printStackTrace();
					// TODO: refactor
					// fallback to graphml 
					fileSink = new FileSinkGraphML();
					//					new FileSinkDGS();
					cacheType = "GraphML";
					fileExtension = "graphml";
				}
			}
//			String dirTree = "cache"+File.separatorChar+"graphs";
			File cacheDir = new File(dirTree);
			if (!cacheDir.exists())
				cacheDir.mkdir();
			fileSink.writeAll(graph, dirTree+File.separatorChar+filenameBasename+"."+fileExtension);
//			System.out.println("writed");
		}
	}
	
	public String getCacheFileExtension() {
		return fileExtension; //"graphml";
	}
	
	public enum UseCacheArguments implements ConsoleCommand {
		cr("cache read: (try to) import previous generated graph from a cached file") {},
		cw("cache write: export generated graph on a cache file") {};
		UseCacheArguments(String helpMessageCore) {
			this.helpMessage = 
//					ConsoleCommand.Utils.
				getPrefix(this,3)+helpMessageCore+"\n";
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
}