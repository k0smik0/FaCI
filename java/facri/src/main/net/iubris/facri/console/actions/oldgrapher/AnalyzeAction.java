package net.iubris.facri.console.actions.oldgrapher;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.console.actions.Analyzer.AnalysisType;
import net.iubris.facri.console.actions.Analyzer.WorldTarget;
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

public class AnalyzeAction implements CommandAction {
	
//	private final static String WORLD_TARGET_FRIENDSHIPS = "f";
//	private final static String WORLD_TARGET_INTERACTIONS = "i";
	
	private final static String WRONG_ARGUMENTS_NUMBER = "analyzer needs two arguments; type 'h' for help\n";
	private final static String WRONG_ARGUMENT = "wrong arguments for analysis: type 'h' for help\n";
	
//	private final static String INTERACTIONS_CACHE_FILENAME = "graph_interactions.graphml"; // use graphml
	
//	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	private final DataParser dataParser;
	private final GraphHolder graphHolder;
	private Analyzer analyzer;
//	
	@Inject
	public AnalyzeAction(
			DataParser dataParser,
			// missing friendships generator
//			GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator,
			GraphHolder graphHolder,
			Analyzer analyzer
			) {
		this.dataParser = dataParser;
//		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
		this.graphHolder = graphHolder;
		this.analyzer = analyzer;
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
				WorldTarget worldTarget = Enum.valueOf(Analyzer.WorldTargetChar.class, worldTargetParam).getWorldTarget();
				
//				Graph graph = 
						worldTarget.prepareGraph(graphHolder, useCache);

				String analysisTypeParam = params[1];
				AnalysisType analysisType = Enum.valueOf(Analyzer.AnalysisTypeChar.class, analysisTypeParam).getAnalysisType();
						
				analyzer.execute(worldTarget, analysisType, useCache, worldTarget.name());
			
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
	
	private UseCache handleUseCache(String[] params) throws FileNotFoundException, JAXBException, XMLStreamException {
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
		A;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message =  
//				HelpAction.tab(1)+"'"+name()+
				"analyze [world] [analysis type] [use_cache]\n"
			+HelpAction.tab(2)+"analyze command needs two arguments:\n"
			+HelpAction.tab(2)+"first argument select 'world' to analyze:\n"
	//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.f +"': analyze friendships 'world'").append(newLine)
	//		.append(tab(3)).append("'"+ AnalyzerLocator.WorldTargetChar.i+"': analyze interactions 'world'").append(newLine)
			+HelpAction.tab(3)+Analyzer.WorldTargetChar.f.getHelpMessage()+"\n"
			+HelpAction.tab(3)+Analyzer.WorldTargetChar.i.getHelpMessage()+"\n"
			+HelpAction.tab(2)+"second argument select analysis type:"+"\n"
			+HelpAction.tab(3)+"'"+ Analyzer.AnalysisTypeChar.mf +"': me and my friends"+"\n"
			+HelpAction.tab(3)+"'"+ Analyzer.AnalysisTypeChar.f +"': my friends"+"\n"
			+HelpAction.tab(3)+"'"+ Analyzer.AnalysisTypeChar.ft +"': my friends and their friends (friends of friends)"+"\n"
			+HelpAction.tab(3)+"'"+ Analyzer.AnalysisTypeChar.t +"': friends of my friends"+"\n"
			+HelpAction.tab(3)+"'"+ Analyzer.AnalysisTypeChar.mft +"': me, my friends, their friends"+"\n"
			+HelpAction.tab(2)+"third argument uses cache for reading or writing generated graph:"+"\n"
			+HelpAction.tab(3)+"'cr': cache read = (try to) import previous generated graph from a cached file\n"
			+HelpAction.tab(3)+"'cw': cache write = export generated graph on a cache file\n"
			+HelpAction.tab(2)+"example: 'a i mf' -> analyze interactions between me and my friends";
	}
	
	class UseCache {
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

}
