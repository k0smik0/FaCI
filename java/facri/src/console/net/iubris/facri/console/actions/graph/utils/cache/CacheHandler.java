package net.iubris.facri.console.actions.graph.utils.cache;

import java.io.File;
import java.io.IOException;

import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphGenerationDoneFunction;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphGenerationFunction;
import net.iubris.facri.parsers.DataParser;
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
//	private final String corpusPrefix;
	
	private String cacheType;
	private FileSink fileSink;
	private String fileExtension;
	
//	private final FacriBerkleyPersister facriBerkleyPersister;
//	private final WorldPersisterService worldPersisterService;
	
	@AssistedInject
	public CacheHandler(
			@Named("cache_type") String cacheType,
//			@Named("me_id") String meId,
			DataParser dataParser,
//			, WorldPersisterService worldPersisterService
			@Assisted String[] params
			) {
		this.cacheType = cacheType;
//		this.corpusPrefix = meId;
//		this.worldPersisterService = worldPersisterService;
		this.fileExtension = cacheType.toLowerCase();
		this.dataParser = dataParser;
		// TODO improve "current" symlink with external parameter
		this.dirTree = "cache"+File.separatorChar+"graphs"+File.separatorChar+"current";
		handlesParams(params);
//		if (!readFromCache || !write) {
//			// parse
//		}
	}
	
	private void handlesParams(String[] actionParams) {
		if (actionParams.length>2) {
			String useCacheString = actionParams[2];		
			UseCacheArguments useCacheArguments = UseCacheArguments.valueOf(useCacheString);
			try {
				switch(useCacheArguments) {
					// if read from cache, do not parse
					case cr:
						readFromCache = true;
						break;
					// we want write cache, so parse for new data
					case cw:
						// TODO persist
						write = true;
						break;
					default:
						break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("wrong argument");
			}
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
	
	public CacheHandler readIfPresent(Graph graph, String cacheFilename,GraphGenerationFunction graphGeneratorFunction,
			GraphGenerationDoneFunction graphGeneratorDoneFunction) throws IOException, JAXBException, XMLStreamException {
		if (readFromCache) {
			// TODO restore populate
//			worldPersisterService.populate();
			readGraph(graph, cacheFilename+"."+getCacheFileExtension());
		} else {
			dataParser.parse();
			graphGeneratorFunction.exec();
			graphGeneratorDoneFunction.exec();
		}
		return this;
	}
	
	private void readGraph(Graph graph, String filename) throws IOException {
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
			// TODO restore persist
//			worldPersisterService.persist();
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
		}
	}
	
	public String getCacheFileExtension() {
		return fileExtension; //"graphml";
	}
	
	/*private static UseCache isUsingCache(String[] actionParams, DataParser dataParser) throws JAXBException, XMLStreamException, IOException {
		boolean cacheRead = false, cacheWrite = false;

		// no cache at all
		if (actionParams.length<3) { 
			dataParser.parse();
		}
		else if (actionParams.length==3) {
			String useCacheString = actionParams[2];		
			UseCacheArguments useCacheArguments = UseCacheArguments.valueOf(useCacheString);
			try {
				switch(useCacheArguments) {
					// if read from cache, do not parse
					case cr:
						cacheRead = true;
						break;
					// write a cache, so parse for new data
					case cw:
						dataParser.parse();
						// TODO persist
						cacheWrite = true;
						break;
					default:
						break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("wrong argument");
			}
		}
		
//		return new UseCache(cacheRead, cacheWrite);
		return null;
	}*/
	
	
	
	/*private static UseCache handleConsoleParamsAndEventuallyParseData(String[] actionParams, DataParser dataParser) throws JAXBException, XMLStreamException, IOException {
		boolean cacheRead = false;
		boolean cacheWrite = false;

		// no cache at all
		if (actionParams.length<3) {
			dataParser.parse();
		}
		else if (actionParams.length==3) {
			String useCacheString = actionParams[2];		
			UseCacheArguments useCacheArguments = UseCacheArguments.valueOf(useCacheString);
			try {
				switch(useCacheArguments) {
					// if read from cache, do not parse
					case cr:
						cacheRead = true;
						break;
					// we want write cache, so parse for new data
					case cw:
						dataParser.parse();
						// TODO persist
						cacheWrite = true;
						break;
					default:
						break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("wrong argument");
			}
		}
		
//		return new UseCache(cacheRead, cacheWrite);
		return null;
	}*/
	
	
	/*public boolean readIfPresentOrParse(Graph graph, String cacheFilename) throws IOException {
		if (readFromCache) {
			read(graph, cacheFilename+"."+getCacheFileExtension());
			return true;
		} 
//		else 
//			dataParser.parse();
		return false;
//		return this;
	}*/
	
	
	
	
	/*public static void handleWritingToCache(UseCache useCache, Graph graph, String cacheFilename) throws IOException {
		if (useCache.write)
			useCache.write(cacheFilename+"."+useCache.getCacheFileExtension(), graph);
	}*/
	
	
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