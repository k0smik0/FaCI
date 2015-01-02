package net.iubris.facri.model.graph;

import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Singleton;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

@Singleton
public class GraphHolder {
	
	private final Graph friendshipsGraph;
	private final Graph interactionsGraph;
	private final Viewer friendshipsGraphViewer;
	private final Viewer interactionsGraphViewer;
	private ViewerPipe fromViewerInteractions;
	
	
	private class NodeViewerListener implements ViewerListener {
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
	};
	
	public GraphHolder() {
		friendshipsGraph = new MultiGraph("Friendships",false,true);
		this.friendshipsGraphViewer = prepareForDisplay(friendshipsGraph);
		interactionsGraph = new MultiGraph("Interactions",false,true);
		this.interactionsGraphViewer = prepareForDisplay(interactionsGraph);
		
		
//		System.out.print("[");		
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("]");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.print("\n");
	}
	
	private Viewer prepareForDisplay(Graph graph) {
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
//		graph.display();
		
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		viewer.enableAutoLayout();
		
		return viewer;
	}
	
	
	
	public void prepareForDisplayFriendships() {
		friendshipsGraphViewer.close();
//		friendshipsGraphViewer.getDefaultView().setVisible(true);
	}
	
	public void prepareForDisplayInteractions() {
		View defaultView = interactionsGraphViewer.getDefaultView();
		if (defaultView!=null) {
			System.out.println("closing "+defaultView);
			defaultView.setVisible(false);
		}
		interactionsGraph.display();
	}

	public Graph getInteractionsGraph() {
		return interactionsGraph;
	}
	
	public ViewerPipe getViewerPipeInteractions(LinkedBlockingQueue<String> queue) {
		fromViewerInteractions = interactionsGraphViewer.newViewerPipe();
		fromViewerInteractions.addViewerListener( new NodeViewerListener(interactionsGraph, queue) );
      fromViewerInteractions.addSink(interactionsGraph);
		return fromViewerInteractions;
	}
	
	public Graph getFriendshipsGraph() {
		return friendshipsGraph;
	}
	
}
