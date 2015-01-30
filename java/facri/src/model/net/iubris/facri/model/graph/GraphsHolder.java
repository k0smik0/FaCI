package net.iubris.facri.model.graph;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.facri.model.graph.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.facri.model.graph.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;
import net.iubris.facri.utils.Pauser;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

@Singleton
public class GraphsHolder {
	
	private final Graph friendshipsGraph;
	private final Graph interactionsGraph;
	private final Viewer friendshipsGraphViewer;
	private final Viewer interactionsGraphViewer;
	private final FriendshipsMouseManagerFactory friendshipsMouseManagerFactory;
	private final InteractionsMouseManagerFactory interactionsMouseManagerFactory;
	
	private boolean interactionsGraphCreated = false;
	private boolean friendshipsGraphCreated = false;
	
	public static Graph temp;
	
	// TODO: refactor constructor
	@Inject
	public GraphsHolder(FriendshipsMouseManagerFactory friendshipsMouseManagerFactory, 
			InteractionsMouseManagerFactory interactionsMouseManagerFactory) {

		this.friendshipsGraph = new MultiGraph("Friendships",false,true);
		this.friendshipsGraphViewer = buildViewer(friendshipsGraph);
		this.friendshipsMouseManagerFactory = friendshipsMouseManagerFactory;
	
		this.interactionsGraph = new MultiGraph("Interactions",false,true);
		this.interactionsGraphViewer = buildViewer(interactionsGraph);
		this.interactionsMouseManagerFactory = interactionsMouseManagerFactory;
		
//		System.out.print("[");
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("]");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Pauser.sleep(100);
//		System.out.print("\n");
	}
	
	public static Viewer buildViewer(Graph graph) {
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
//		graph.addAttribute("ui.speed");
		
//		System.out.println("graph: "+graph);
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel
				.GRAPH_IN_ANOTHER_THREAD);
//				.GRAPH_IN_GUI_THREAD);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
//		viewer.enableAutoLayout(new LinLog(true));
//		viewer.enableAutoLayout(new SpringBox(true));
		viewer.enableAutoLayout();
		viewer.enableXYZfeedback(true);
		
		return viewer;
	}
	
	public static ViewPanel buildView(Viewer viewer) {
		ViewPanel view = viewer.getDefaultView();
		if (view==null) {
			view = viewer.addDefaultView(true);
			view.setEnabled(true);
		}
		return view;
	}
		
	public void prepareForDisplayFriendships() {
		ViewPanel view = buildView(friendshipsGraphViewer);
		view.setMouseManager(friendshipsMouseManagerFactory.create(friendshipsGraphViewer));
	}
	
	public void prepareForDisplayInteractions() {
		ViewPanel view = buildView(interactionsGraphViewer);
		view.setMouseManager(interactionsMouseManagerFactory.create(interactionsGraphViewer));
	}
	
	
	public Viewer getInteractionsGraphViewer() {
		return interactionsGraphViewer;
	}
	public Viewer getFriendshipsGraphViewer() {
		return friendshipsGraphViewer;
	}
	
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
	
	

	/*public ViewerPipe getViewerPipeInteractions(LinkedBlockingQueue<String> queue) {
		fromViewerInteractions = interactionsGraphViewer.newViewerPipe();
//		fromViewerInteractions.addViewerListener( new NodeViewerListener(interactionsGraph, queue) );
      fromViewerInteractions.addSink(interactionsGraph);
		return fromViewerInteractions;
	}*/
	
	/*private class NodeViewerListener implements ViewerListener {
	private final Graph graph;
	private LinkedBlockingQueue<String> queue;
//	private boolean loop = true;
	
	public NodeViewerListener(Graph graph, LinkedBlockingQueue<String> queue) {
		this.graph = graph;
		this.queue = queue;
	}
	@Override
	public void viewClosed(String viewName) {
//		loop = false;
	}
	@Override
	public void buttonReleased(String id) {}
	@Override
	public void buttonPushed(String id) {
		System.out.println(id+": "+graph.getNode(id).getDegree());
		queue.add(id);
	}
//	public void doLoop() {
//		loop = true;
//	}
};*/
}
