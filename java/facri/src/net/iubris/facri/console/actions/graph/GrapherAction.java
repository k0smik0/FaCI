package net.iubris.facri.console.actions.graph;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.console.actions.graph.Grapher.GraphType;
import net.iubris.facri.console.actions.graph.Grapher.WorldTarget;
import net.iubris.facri.model.graph.GraphHolder;
import net.iubris.facri.parsers.DataParser;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceGraphML;

public class GrapherAction implements CommandAction {
	
	private final static String WRONG_ARGUMENTS_NUMBER = "analyzer needs two arguments; type 'h' for help\n";
	private final static String WRONG_ARGUMENT = "wrong arguments for analysis: type 'h' for help\n";
	
	
	private final DataParser dataParser;
	private final GraphHolder graphHolder;
	private Grapher grapher;

	@Inject
	public GrapherAction(
			DataParser dataParser,
			// missing friendships generator
			GraphHolder graphHolder,
			Grapher grapher
			) {
		this.dataParser = dataParser;
		this.graphHolder = graphHolder;
		this.grapher = grapher;
	}

	@Override
	public void exec(Console console, String... params) throws Exception {
		try {
			if (params==null || (params!=null && params.length<2)) {
				handleError(console);
				return;
			}
			
			UseCache useCache = handleUseCache(params);
			
			try {
				String worldTargetParam = params[0];
				WorldTarget worldTarget = Enum.valueOf(Grapher.WorldTargetChar.class, worldTargetParam).getWorldTarget();
				
//				Graph graph = 
						worldTarget.prepareGraph(graphHolder, useCache);

				String analysisTypeParam = params[1];
				GraphType analysisType = Enum.valueOf(Grapher.GrapherTypeChar.class, analysisTypeParam).getAnalysisType();
						
				grapher.execute(worldTarget, analysisType, useCache, worldTarget.name());
				
//				graphHolder.
//				graph.getNodeIterator().next().get
			
			} catch(IllegalArgumentException e) {
				console.printf(WRONG_ARGUMENT);
			}
			
			/*
//			Graph graph = null;
			switch (worldTarget) {
			case friendships:
				// TODO use friendships generator
				break;
			case interactions:
//				graphHolder.prepareForDisplayInteractions();
//				graph = graphHolder.getInteractionsGraph();
//				if (useCache.read) {
//					useCache.read(INTERACTIONS_CACHE_FILENAME, graph);
//				}
				handleAnalysis(analysisType);
//				if (useCache.write) {
//					useCache.write(INTERACTIONS_CACHE_FILENAME, graph);					
//				}
				
				
				LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
				ViewerPipe viewerPipeInteractions = graphHolder.getViewerPipeInteractions(queue);
				viewerPipeInteractions.pump();
//				ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();				
//				Runnable runnable = new Runnable() {
//					@Override
//					public void run() {
//						while(true) {
//							try {
//								String polled = queue.poll(50, TimeUnit.MILLISECONDS);
//								System.out.println(polled);
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//							viewerPipeInteractions.pump();
//						}
//					}
//				};
//				newSingleThreadExecutor.execute(runnable);
				
				
				break;
			default:
				console.printf(WORLD_TARGET_WRONG_ARGUMENT);
				break;
			}
			*/
			
		} catch (IllegalArgumentException e) {
			handleError(console);
		}		
	}
	
	private UseCache handleUseCache(String[] params) throws JAXBException, XMLStreamException, IOException {
		boolean cacheRead = false, cacheWrite = false;
		// bad, but working
		if (params.length==3) {
			String useCache = params[2];
			// if read from cache, do not parse
			if (useCache.equals("cr")) {
				cacheRead = true;
			}
			// write a cache, so parse for new data 
			if (useCache.equals("cw")) {
				dataParser.parse();
				cacheWrite = true;
			}
		} else {
			// no cache choise at all, so parse
			dataParser.parse();
		}
		return new UseCache(cacheRead, cacheWrite);
	}
	
	private void handleError(Console console) {
		console.printf(WRONG_ARGUMENTS_NUMBER);
	}
	
	public enum AnalyzeCommand implements ConsoleCommand {
		G;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message =  
				"graph [world] [analysis type] [use_cache]\n"
			+HelpAction.tab(2)+"'graph' command needs two arguments:\n"
			+HelpAction.tab(2)+"first argument select 'world' to analyze:\n"
			+Grapher.WorldTargetChar.f.getHelpMessage()
			+Grapher.WorldTargetChar.i.getHelpMessage()
			+HelpAction.tab(2)+"second argument select analysis type:\n"
			+Grapher.GrapherTypeChar.mf.getHelpMessage()
			+Grapher.GrapherTypeChar.f.getHelpMessage()
			+Grapher.GrapherTypeChar.ft.getHelpMessage()
			+Grapher.GrapherTypeChar.t.getHelpMessage()
			+Grapher.GrapherTypeChar.mft.getHelpMessage()
			+HelpAction.tab(2)+"third argument uses cache for reading or writing generated graph:"+"\n"
			+UseCacheChar.cr.getHelpMessage()
			+UseCacheChar.cw.getHelpMessage()
			+HelpAction.tab(2)+"example: 'g i mf' -> graphs interactions between me and my friends";
	}
	
	public class UseCache {
		boolean read;
		boolean write;
		public UseCache(boolean cacheRead, boolean cacheWrite) {
			this.read = cacheRead;
			this.write = cacheWrite;
		}
		public void read(String filename, Graph graph) throws IOException {
			String filenamePath = "cache"+File.separatorChar+filename;
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
		public void write(String filename, Graph graph) throws IOException {
			FileSink fileSink = 
					new FileSinkGraphML();
//					new FileSinkDGS();
			File cacheDir = new File("cache");
			if (!cacheDir.exists())
				cacheDir.mkdir();
			fileSink.writeAll(graph, "cache"+File.separatorChar+filename);
		}
		public String getCacheFileExtension() {
			return "graphml";
		}
	}
	public enum UseCacheChar implements ConsoleCommand {
		cw("cache read: (try to) import previous generated graph from a cached file") {},
		cr("cache write: export generated graph on a cache file") {};
		UseCacheChar(String helpMessageCore) {
			this.helpMessage = ConsoleCommand.Utils.getPrefix(this,3)+helpMessageCore+"\n";
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
}
