/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GrapherAction.java) is part of facri.
 * 
 *     GrapherAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GrapherAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.console.actions.graph.grapher;

import java.io.Console;
import java.io.IOException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.cache.CacheHandler;
import net.iubris.faci.cache.CacheHandler.CacheHandlerFactory;
import net.iubris.faci.cache.CacheHandler.UseCacheArguments;
import net.iubris.faci.console.actions.graph.grapher.GrapherExecutor.GraphTypeCommand;
import net.iubris.faci.console.actions.graph.grapher.GrapherExecutor.WorldTypeCommand;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.actions.HelpAction;
import net.iubris.heimdall.command.ConsoleCommand;

public class GrapherAction implements CommandAction {
	
//	private final GraphsHolder graphsHolder;
	private final GrapherExecutor grapherExecutor;
	private final CacheHandlerFactory useCacheFactory;

	@Inject
	public GrapherAction(
//			GraphsHolder graphHolder,
			GrapherExecutor grapherExecutor,
			CacheHandlerFactory useCacheFactory
			) {
//		this.graphsHolder = graphHolder;
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
			
			String graphTypeParam = params[0];
			String worldTypeParam = params[1];
				
			grapherExecutor.execute(
				// graph type
				Enum.valueOf(GraphTypeCommand.class, graphTypeParam).getGraphType()/*.prepareGraph(graphsHolder)*/,
				// world type
				Enum.valueOf(WorldTypeCommand.class, worldTypeParam).getWorldType(), 
				// using cache
				useCache
			);
				
//				fixCSS();
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
			+UseCacheArguments.cwg.getHelpMessage()
			+HelpAction.tab(2)+"example: 'g i mf' -> graphs interactions between me and my friends";
	}
}
