package net.iubris.facri.model.graph;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.facri.model.graph.eventmanagers.InternalMouseManager.InternalMouseManagerFactory;
import net.iubris.facri.utils.Pauser;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

@Singleton
public class GraphsHolder {
	
	private final Graph friendshipsGraph;
	private final Graph interactionsGraph;
	private final Viewer friendshipsGraphViewer;
	private final Viewer interactionsGraphViewer;
	private final InternalMouseManagerFactory internalMouseManagerFactory;
	
//	private ViewerPipe fromViewerInteractions;
//	final World world;
//	private final DataParser dataParser;
	
	private boolean interactionsGraphCreated = false;
	private boolean friendshipsGraphCreated = false;
	
	
	
	/*private class NodeViewerListener implements ViewerListener {
		private final Graph graph;
		private LinkedBlockingQueue<String> queue;
//		private boolean loop = true;
		
		public NodeViewerListener(Graph graph, LinkedBlockingQueue<String> queue) {
			this.graph = graph;
			this.queue = queue;
		}
		@Override
		public void viewClosed(String viewName) {
//			loop = false;
		}
		@Override
		public void buttonReleased(String id) {}
		@Override
		public void buttonPushed(String id) {
			System.out.println(id+": "+graph.getNode(id).getDegree());
			queue.add(id);
		}
//		public void doLoop() {
//			loop = true;
//		}
	};*/
	
	// TODO: refactor constructor
	@Inject
	public GraphsHolder(/*World world*//*, DataParser dataParser*/InternalMouseManagerFactory internalMouseManagerFactory) {
		this.internalMouseManagerFactory = internalMouseManagerFactory;
		//		this.world = world;
//		this.dataParser = dataParser;
		this.friendshipsGraph = new MultiGraph("Friendships",false,true);
		this.friendshipsGraphViewer = prepareForDisplay(friendshipsGraph);
		this.interactionsGraph = new MultiGraph("Interactions",false,true);
		this.interactionsGraphViewer = prepareForDisplay(interactionsGraph);
		
//		System.out.print("[");
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("]");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Pauser.sleep(100);
//		System.out.print("\n");
	}
	
//	public World getWorld() {
//		return world;
//	}
	
	public void reparseCSS() {
		if (friendshipsGraph!=null) {
			friendshipsGraph.setAttribute("ui.stylesheet", "");
			friendshipsGraph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+"friendships.css')");
//			Viewer viewer = graph.display();
//			viewer.enableAutoLayout();
		}
		if (interactionsGraph!=null) {
			interactionsGraph.setAttribute("ui.stylesheet", "");
			interactionsGraph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+"interactions.css')");
		}
	}
	
	private Viewer prepareForDisplay(Graph graph) {
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
//		graph.addAttribute("ui.speed");
		
//		System.out.println("graph: "+graph);
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//		System.out.println("viewer: "+viewer);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		viewer.enableAutoLayout();
		
		return viewer;
	}
	
	
	public void prepareForDisplayFriendships() {
		View view = friendshipsGraphViewer.getDefaultView();
		if (view==null) {
			view = friendshipsGraphViewer.addDefaultView(true);
		}
		view.setVisible(true);
		
		view.setMouseManager(internalMouseManagerFactory.create(friendshipsGraphViewer));
//		view.setShortcutManager(new InternalShortcutManager(friendshipsGraphViewer));
	}
	
	public void prepareForDisplayInteractions() {
		View view = interactionsGraphViewer.getDefaultView();
		if (view==null) {
			view = interactionsGraphViewer.addDefaultView(true);
		}
		view.setVisible(true);
		
		view.setMouseManager(internalMouseManagerFactory.create(interactionsGraphViewer));
//		view.setShortcutManager(new InternalShortcutManager(interactionsGraphViewer));
	}
	
	public Viewer getInteractionsGraphViewer() {
		return interactionsGraphViewer;
	}
	public Viewer getFriendshipsGraphViewer() {
		return friendshipsGraphViewer;
	}
	
	
		
	/*public ViewerPipe getViewerPipeInteractions(LinkedBlockingQueue<String> queue) {
		fromViewerInteractions = interactionsGraphViewer.newViewerPipe();
//		fromViewerInteractions.addViewerListener( new NodeViewerListener(interactionsGraph, queue) );
      fromViewerInteractions.addSink(interactionsGraph);
		return fromViewerInteractions;
	}*/
	
	public void setInteractionsGraphsCreated(boolean isCreated) {
		interactionsGraphCreated = isCreated;
	}
	public boolean isInteractionsGraphCreated() {
		return interactionsGraphCreated;
	}
	public void setFriendshipsGraphsCreated(boolean isCreated) {
		friendshipsGraphCreated = isCreated;
	}
	public boolean isFriendshipsGraphCreated() {
		return friendshipsGraphCreated;
	}
	
	public Graph getFriendshipsGraph() {
		return friendshipsGraph;
	}
	
	public Graph getInteractionsGraph() {
		return interactionsGraph;
	}
}
