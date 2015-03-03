/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphCloner.java) is part of facri.
 * 
 *     GraphCloner.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphCloner.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.cloner;


import java.io.File;

import javax.inject.Inject;

import net.iubris.faci.grapher.holder.core.ViewUtils;
import net.iubris.faci.grapher.holder.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.faci.grapher.holder.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AdjacencyListGraph;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

public class GraphCloner {
	
	private final FriendshipsMouseManagerFactory friendshipsMouseManagerFactory;
	private final InteractionsMouseManagerFactory interactionsMouseManagerFactory;

	@Inject
	public GraphCloner(
			FriendshipsMouseManagerFactory friendshipsMouseManagerFactory, 
			InteractionsMouseManagerFactory interactionsMouseManagerFactory) {
				this.friendshipsMouseManagerFactory = friendshipsMouseManagerFactory;
				this.interactionsMouseManagerFactory = interactionsMouseManagerFactory;
	}

	public GraphDataHolder copyWithMouseManager(Graph originalGraph, String titleAndIdForNewGraph) {
		GraphDataHolder graphDataHolder = new GraphDataHolder();
		graphDataHolder.graph = copy(originalGraph, titleAndIdForNewGraph);
		
		String graphId = originalGraph.getId().split(" ")[0];
		graphDataHolder.graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+graphId.toLowerCase()+".css')");
		
		graphDataHolder.viewer = ViewUtils.buildViewer(graphDataHolder.graph);
		graphDataHolder.view = ViewUtils.buildView(graphDataHolder.viewer);
		
		graphDataHolder.graph.setAttribute("ui.title", titleAndIdForNewGraph);
		
		if (originalGraph.getId().toLowerCase().contains("friendships")) {
			graphDataHolder.view.setMouseManager(friendshipsMouseManagerFactory.create(graphDataHolder.viewer));
		} else if (originalGraph.getId().toLowerCase().contains("interactions")) {
			graphDataHolder.view.setMouseManager(interactionsMouseManagerFactory.create(graphDataHolder.viewer));
		}
		
		return graphDataHolder;
	}
	
	public GraphDataHolder clone(Graph originalGraph) {
		return copyWithMouseManager(originalGraph,originalGraph.getId());
	}
	
	public static class GraphDataHolder {
		private Graph graph;
		private ViewPanel view;
		private Viewer viewer;
		
		public Graph getGraph() {
			return graph;
		}
		public void setGraph(Graph graph) {
			this.graph = graph;
		}		
		public ViewPanel getView() {
			return view;
		}
		public Viewer getViewer() {
			return viewer;
		}
		public void setView(ViewPanel view) {
			this.view = view;
		}
		public void setViewer(Viewer viewer) {
			this.viewer = viewer;
		}
	}
	
	/**
	 * because Graphs.clone copy also id, and it's wrong
	 * @param graph
	 * @return
	 */
	public static Graph copy(Graph graph, String titleAndIdForNewGraph) {
		Graph copy;
		
		try {
			Class<? extends Graph> cls = graph.getClass();
			copy = cls.getConstructor(String.class).newInstance(titleAndIdForNewGraph);
		} catch (Exception e) {
			System.out.println("copy using adjacency list");
			copy = new AdjacencyListGraph(titleAndIdForNewGraph);
		}
		
		Graphs.copyAttributes(graph, copy);
//copy.getAttributeKeySet().forEach(a->System.out.println(a));
		
		copyNodes(graph, copy);
		copyEdges(graph, copy);
		
		return copy;
	}
	
	public static Graph copy(Graph graph) {
		return copy(graph, graph.getId());
	}

	// from graphstream Graphs.clone
	public static void copyNodes(Graph graphSource, Graph graphCopy) {
		for (int i = 0; i < graphSource.getNodeCount(); i++) {
			Node nodeSource = graphSource.getNode(i);
			Node nodeTarget = graphCopy.addNode(nodeSource.getId());

			Graphs.copyAttributes(nodeSource, nodeTarget);
		}
	}
	// from graphstream Graphs.clone
	public static void copyEdges(Graph graphSource, Graph copy) {
		for (int i = 0; i < graphSource.getEdgeCount(); i++) {
			Edge edgeSource = graphSource.getEdge(i);
			Edge edgeTarget = copy.addEdge(edgeSource.getId(), edgeSource.getSourceNode()
					.getId(), edgeSource.getTargetNode().getId(), edgeSource
					.isDirected());

			Graphs.copyAttributes(edgeSource, edgeTarget);
		}
	}
		
	
	public interface GraphClonerFactory {
		GraphCloner create(Graph graph, String title);
	}
	
}
