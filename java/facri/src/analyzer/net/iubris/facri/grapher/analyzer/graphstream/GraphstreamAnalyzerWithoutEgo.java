package net.iubris.facri.grapher.analyzer.graphstream;

import net.iubris.facri.model.graph.utils.GraphCloner;
import net.iubris.facri.utils.Printer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;


public class GraphstreamAnalyzerWithoutEgo extends AbstractGraphstreamAnalyzer {
	
	// too bad, but it works and I don't have too much time
	@Override
	protected Graph buildGraph() {
		Graph graph = 
					super.graphCopyDataHolder.getGraph();
		int removed = NoZeroDegree.removeZeroDegreeNodes(this.graph);
		this.graph.removeNode(egoNode);
		Printer.println("Dichotomized "+this.graph.getId()+"removing node with degree=0: removed "+removed+" nodes; removed also Ego node");
		return graph;
	}
	
	private final String ss = "_without_ego";
	
	@AssistedInject
	public GraphstreamAnalyzerWithoutEgo(@Assisted Graph graph, @Assisted Node egoNode, GraphCloner graphCloner) {
		super(graph, egoNode, graphCloner);
	}

	@Override
	protected String buildSpecifiedSuffixForOutputFiles() {
		return ss;
	}
	
	public interface GraphstreamAnalyzerWithoutEgoFactory {
		GraphstreamAnalyzerWithoutEgo create(Graph graph, Node node);
	}
	
}
