/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphsHolder.java) is part of facri.
 * 
 *     GraphsHolder.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphsHolder.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.holder.core;


import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.faci.grapher.holder._di.annotations.graph.GraphFriendships;
import net.iubris.faci.grapher.holder._di.annotations.graph.GraphInteractions;
import net.iubris.faci.grapher.holder._di.annotations.graphviewer.FriendshipsGraphViewer;
import net.iubris.faci.grapher.holder._di.annotations.graphviewer.InteractionsGraphViewer;
import net.iubris.faci.grapher.holder.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.faci.grapher.holder.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;

import org.graphstream.graph.Graph;
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
	public static final String graph_file_name = "graph_file_name";
	
	// TODO: refactor constructor usind DI
	@Inject
	public GraphsHolder(
			@GraphFriendships Graph friendshipsGraph,
			@FriendshipsGraphViewer Viewer friendshipsGraphViewer,
			FriendshipsMouseManagerFactory friendshipsMouseManagerFactory,
			
			@GraphInteractions Graph interactionsGraph,
			@InteractionsGraphViewer Viewer interactionsGraphViewer, 
			InteractionsMouseManagerFactory interactionsMouseManagerFactory) {

		this.friendshipsGraph = 
//				new MultiGraph("Friendships",false,true);
				friendshipsGraph;
		this.friendshipsGraphViewer = 
//				ViewUtils.buildViewer(friendshipsGraph);
				friendshipsGraphViewer;
		this.friendshipsMouseManagerFactory = friendshipsMouseManagerFactory;
	
		this.interactionsGraph = 
//				new MultiGraph("Interactions",false,true);
				interactionsGraph;
		this.interactionsGraphViewer = 
//				ViewUtils.buildViewer(interactionsGraph);
				interactionsGraphViewer;
		this.interactionsMouseManagerFactory = interactionsMouseManagerFactory;
		
//		System.out.print("[");
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("]");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//		Pauser.sleep(100);
//		System.out.print("\n");
	}
	
	public void prepareForDisplayFriendships() {
		ViewPanel view = ViewUtils.buildView(friendshipsGraphViewer);
		view.setMouseManager(friendshipsMouseManagerFactory.create(friendshipsGraphViewer));
	}
	
	public void prepareForDisplayInteractions() {
		ViewPanel view = ViewUtils.buildView(interactionsGraphViewer);
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
