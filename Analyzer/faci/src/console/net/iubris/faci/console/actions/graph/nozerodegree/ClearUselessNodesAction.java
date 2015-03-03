/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ClearUselessNodesAction.java) is part of facri.
 * 
 *     ClearUselessNodesAction.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ClearUselessNodesAction.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.console.actions.graph.nozerodegree;

import java.io.Console;

import javax.inject.Inject;

import net.iubris.faci.analyzer.graphstream.NoZeroDegree;
import net.iubris.faci.grapher.cloner.GraphCloner;
import net.iubris.faci.grapher.cloner.GraphCloner.GraphDataHolder;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

public class ClearUselessNodesAction implements CommandAction {
	
	private final GraphsHolder graphHolder;
//	private final FriendshipsMouseManagerFactory friendshipsMouseManagerFactory;
//	private final InteractionsMouseManagerFactory interactionsMouseManagerFactory;
//	private final World world;
	private final GraphCloner graphCloner;
	
//	private static String WRONG = "wrong arguments";

	@Inject
	public ClearUselessNodesAction(
			GraphsHolder graphsHolder
			, GraphCloner graphCloner
//			,FriendshipsMouseManagerFactory friendshipsMouseManagerFactory, 
//			InteractionsMouseManagerFactory interactionsMouseManagerFactory
			) {
		this.graphHolder = graphsHolder;
		this.graphCloner = graphCloner;
		
//		this.friendshipsMouseManagerFactory = friendshipsMouseManagerFactory;
//		this.interactionsMouseManagerFactory = interactionsMouseManagerFactory;
	}
	
	@Override
	public void exec(Console console, String... params) throws Exception {
		
		if (graphHolder.isFriendshipsGraphCreated()) {
//			Holder doGraph = doGraph(graphHolder.getFriendshipsGraph(), "friendships");
//			doGraph.view.setMouseManager(friendshipsMouseManagerFactory.create(doGraph.viewer));
			GraphDataHolder clonedHolder = graphCloner.copyWithMouseManager(graphHolder.getFriendshipsGraph(), "Friendships - without zero edges nodes");
//			Graph clonedGraph = clonedHolder.getGraph();
//			clonedGraph.setAttribute("ui.title", "Friendships - without zero edges nodes");
			NoZeroDegree.removeZeroDegreeNodes(clonedHolder.getGraph());
			resetView(clonedHolder.getViewer());			
		}
		
		if (graphHolder.isInteractionsGraphCreated()) {
//			Holder doGraph = doGraph(graphHolder.getInteractionsGraph(), "interactions");
//			doGraph.view.setMouseManager(interactionsMouseManagerFactory.create(doGraph.viewer));
			GraphDataHolder clonedHolder = graphCloner.copyWithMouseManager(graphHolder.getInteractionsGraph(), "Interactions - without zero edges nodes");
//			Graph clonedGraph = clonedHolder.getGraph();
//			clonedGraph.setAttribute("ui.title", "Interactions - without zero edges nodes");
			NoZeroDegree.removeZeroDegreeNodes(clonedHolder.getGraph());
			resetView(clonedHolder.getViewer());
		}

	}
	
	private void resetView(Viewer viewer) {
		Camera camera = viewer.getDefaultView().getCamera();
		camera.setViewPercent(0.1);
		camera.setViewPercent(1);
	}
	
	/*private Holder doGraph(Graph graph, String css) {
		Graph clonedGraph = Graphs.clone( graph );
		graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+css+".css')");
		Holder holder = new Holder();
		holder.viewer = GraphsHolder.buildViewer(clonedGraph);
//		GraphsHolder.prepareForDisplay(viewer, mouseManagerFactory);
		holder.view = GraphsHolder.buildView(holder.viewer);
//		view.setMouseManager(mouseManagerFactory.create(viewer));
		
		removeUseless(clonedGraph);
		Camera camera = holder.viewer.getDefaultView().getCamera();
		
		camera.setViewPercent(0.1);
//		mon.notify();
		camera.setViewPercent(1);
//		System.out.println("1");
//		camera.resetView();
		return holder;
	}
	
	class Holder {
		ViewPanel view;
		Viewer viewer;
	}*/
	
//	public static int removeZeroDegreeNodes(Graph graph) {
//		AtomicInteger removed = new AtomicInteger(0);
//		Stream<Node> stream = StreamSupport.stream(graph.spliterator(), true);
//		stream.forEach(node -> {
//			if (node.getDegree()==0) {
//				graph.removeNode(node);
//				removed.incrementAndGet();
//			}
//		});
//		Iterator<Node> iterator = graph.iterator();
//		while (iterator.hasNext()) {
//			Node node = iterator.next();
//			if (node.getDegree()==0) {
////				try {
//					iterator.remove();
//					removed.incrementAndGet();
////				} catch(NullPointerException npe) {}
//			}
//		}
//		return removed.get();
//	}
	
//	enum SearchArg {
//		u, // user by id
//		n, // user by name
//		m; // me
//	}
	
//	private boolean notEnoughArgs(String[] params, Console console) {
//		if (params.length < 2) {
//			console.printf(WRONG);
//			return true;
//		}
//		return false;
//	}
	
	/*private void doWithOptional(Optional<User> optionalUser) {
		if (optionalUser.isPresent())
			doWhenUserFound( optionalUser.get() );
		else
			System.out.println("no user found");
	}*/
	
//	private Optional<User> doWhenUserFound(User user) {
////		markUser( graphHolder.getInteractionsGraph(), user.getUid() );
//		markUser( user.getUid() );
//		return Optional.empty();
//	}
	
	/*private void markUser(Graph graph, String userId) {
		if (graph!=null) {
//			graph.getNode(userId).addAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
//			graph.getNode(userId).setAttribute("ui.marked");
//			System.out.println( graph.getNode(userId).getAttribute("ui.marked") );
			graph.getNode(userId).setAttribute("ui.class","marked");
		}
	}*/
//	private void markUser(String userId) {
//		if (graphHolder.isFriendshipsGraphCreated())
//			graphHolder.getFriendshipsGraph().getNode(userId).setAttribute("ui.class","marked");
//		if (graphHolder.isInteractionsGraphCreated())
//			graphHolder.getInteractionsGraph().getNode(userId).setAttribute("ui.class","marked");
//	}
	
	public enum ClearUselessNodesActionCommand implements ConsoleCommand {
		Z;
		private final String helpMessage = "remove nodes with zero degree";
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
	}

}
