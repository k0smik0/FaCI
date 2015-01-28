package net.iubris.facri.console.actions.graph.nozerodegree;

import java.io.Console;
import java.io.File;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.graph.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.facri.model.graph.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;
import net.iubris.heimdall.actions.CommandAction;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

public class ClearUselessNodesAction implements CommandAction {
	
	private final GraphsHolder graphHolder;
	private final FriendshipsMouseManagerFactory friendshipsMouseManagerFactory;
	private final InteractionsMouseManagerFactory interactionsMouseManagerFactory;
//	private final World world;
	
//	private static String WRONG = "wrong arguments";

	@Inject
	public ClearUselessNodesAction(
			GraphsHolder graphsHolder, 
			FriendshipsMouseManagerFactory friendshipsMouseManagerFactory, 
			InteractionsMouseManagerFactory interactionsMouseManagerFactory) {
		this.graphHolder = graphsHolder;
		
		this.friendshipsMouseManagerFactory = friendshipsMouseManagerFactory;
		this.interactionsMouseManagerFactory = interactionsMouseManagerFactory;
	}
	
	@Override
	public void exec(Console console, String... params) throws Exception {
		
		if (graphHolder.isFriendshipsGraphCreated()) {
			Holder doGraph = doGraph(graphHolder.getFriendshipsGraph(), "friendships");
			doGraph.view.setMouseManager(friendshipsMouseManagerFactory.create(doGraph.viewer));
		}
		
		if (graphHolder.isInteractionsGraphCreated()) {
			Holder doGraph = doGraph(graphHolder.getInteractionsGraph(), "interactions");
			doGraph.view.setMouseManager(interactionsMouseManagerFactory.create(doGraph.viewer));
		}

	}
	
	private Holder doGraph(Graph graph/*, MouseManagerFactory<M> mouseManagerFactory*/, String css) {
		Graph friendshipsGraph = Graphs.clone( graph );
		graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+css+".css')");
		Holder holder = new Holder();
		holder.viewer = GraphsHolder.buildViewer(friendshipsGraph);
//		GraphsHolder.prepareForDisplay(viewer, mouseManagerFactory);
		holder.view = GraphsHolder.buildView(holder.viewer);
//		view.setMouseManager(mouseManagerFactory.create(viewer));
		
		removeUseless(friendshipsGraph);
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
	}
	
	private void removeUseless(final Graph graph) {
		Spliterator<Node> spliterator = graph.spliterator();
		Stream<Node> stream = StreamSupport.stream(spliterator, true);
		stream.forEach(n->{
			if (n.getDegree()==0)
				graph.removeNode(n);
		});
	}
	
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
