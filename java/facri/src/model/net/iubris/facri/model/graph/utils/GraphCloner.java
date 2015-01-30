package net.iubris.facri.model.graph.utils;


import javax.inject.Inject;
import java.io.File;

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.graph.eventmanagers.FriendshipsMouseManager.FriendshipsMouseManagerFactory;
import net.iubris.facri.model.graph.eventmanagers.InteractionsMouseManager.InteractionsMouseManagerFactory;

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
		graphDataHolder.graph = 
//				Graphs.clone(originalGraph);
				copy(originalGraph, titleAndIdForNewGraph);
		
		String graphId = originalGraph.getId().split(" ")[0];
//System.out.println(graphDataHolder.graph.getId());
		graphDataHolder.graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+graphId.toLowerCase()+".css')");
		
		graphDataHolder.viewer = GraphsHolder.buildViewer(graphDataHolder.graph);
		graphDataHolder.view = GraphsHolder.buildView(graphDataHolder.viewer);
		
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
