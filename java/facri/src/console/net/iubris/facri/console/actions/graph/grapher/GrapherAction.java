package net.iubris.facri.console.actions.graph.grapher;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.grapher.factories.CacheHandlerFactory;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.facri.console.actions.graph.grapher.GrapherExecutor.WorldTypeCommand;
import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler;
import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler.UseCacheArguments;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class GrapherAction implements CommandAction {
	
//	private final static String WRONG_ARGUMENTS_NUMBER = "analyzer needs two arguments; type 'h' for help\n";
//	private final static String WRONG_ARGUMENT = "wrong arguments for analysis: type 'h' for help\n";
	
//	private final DataParser dataParser;
	private final GraphsHolder graphsHolder;
	private final GrapherExecutor grapherExecutor;
	private final CacheHandlerFactory useCacheFactory;

	@Inject
	public GrapherAction(
//			DataParser dataParser,
			// missing friendships generator
			GraphsHolder graphHolder,
			GrapherExecutor grapherExecutor
			, CacheHandlerFactory useCacheFactory
			) {
//		this.dataParser = dataParser;
		this.graphsHolder = graphHolder;
		this.grapherExecutor = grapherExecutor;
		this.useCacheFactory = useCacheFactory;
	}

	@Override
	public void exec(Console console, String... params) throws JAXBException, XMLStreamException, IOException {
//		try {
			if (params==null || (params.length<2)) {
				handleError(console, WRONG_ARGUMENTS_NUMBER);
				return;
			}
			
			CacheHandler useCache = useCacheFactory.create(params);
			
//			try {
				String graphTypeParam = params[0];
				String worldTypeParam = params[1];
				
				grapherExecutor.execute(
					// graph type
					Enum.valueOf(GraphTypeCommand.class, graphTypeParam).getGraphType().prepareGraph(graphsHolder),
					// world type
					Enum.valueOf(WorldTypeCommand.class, worldTypeParam).getWorldType(),
					useCache
				);
				
				fixCSS();
//			} catch(IllegalArgumentException e) {
//				console.printf(WRONG_ARGUMENT);
//			}
			
			/*
//			Graph graph = null;
			switch (worldTarget) {
			case friendships:
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
			
//		} catch (IllegalArgumentException e) {
//			handleError(console);
//		}
	}
	
	private void fixCSS() {
		if (graphsHolder.isFriendshipsGraphCreated()) {
			graphsHolder.getFriendshipsGraph().setAttribute("ui.stylesheet", "url('friendships.css')");
		}
		if (graphsHolder.isInteractionsGraphCreated()) {
			graphsHolder.getInteractionsGraph().setAttribute("ui.stylesheet", "url('interactions.css')");
		}		
	}
	public enum GrapherCommand implements ConsoleCommand {
		G;
		@Override
		public String getHelpMessage() {
			return message;
		}
		private String message =  
			"graph [world] [actors] [use_cache]\n"
			+HelpAction.tab(2)+"'graph' command needs two arguments:\n"
			+HelpAction.tab(2)+"first argument select 'world' to graph:\n"
			+GrapherExecutor.GraphTypeCommand.f.getHelpMessage()
			+GrapherExecutor.GraphTypeCommand.i.getHelpMessage()
			+HelpAction.tab(2)+"second argument select 'actors' type:\n"
			+GrapherExecutor.WorldTypeCommand.mf.getHelpMessage()
			+GrapherExecutor.WorldTypeCommand.f.getHelpMessage()
			+GrapherExecutor.WorldTypeCommand.ft.getHelpMessage()
			+GrapherExecutor.WorldTypeCommand.t.getHelpMessage()
			+GrapherExecutor.WorldTypeCommand.mft.getHelpMessage()
			+HelpAction.tab(2)+"third argument uses cache for reading or writing generated graph:"+"\n"
			+UseCacheArguments.cr.getHelpMessage()
			+UseCacheArguments.cw.getHelpMessage()
			+HelpAction.tab(2)+"example: 'g i mf' -> graphs interactions between me and my friends";
	}
}
