package net.iubris.facri.grapher.analyzer.graphstream;

import net.iubris.facri.model.graph.utils.GraphCloner;
import net.iubris.facri.utils.Printer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;


public class GraphstreamAnalyzerSuffix extends AbstractGraphstreamAnalyzer {
	
// too bad, but it works and I don't have too much time
	@Override
	protected void dichotomizeGraph(Graph graph) {
		int removed = NoZeroDegree.removeZeroDegreeNodes(this.graph);
//		graph.removeNode(egoNode);
		Printer.println("Dichotomized '"+graph.getId()+"' removing node with degree=0: removed "+removed+" nodes.");
//		; removed also Ego node");
	}
	
	private final String ss;
	
	@AssistedInject
	public GraphstreamAnalyzerSuffix(@Assisted Graph graph, @Assisted Node egoNode, GraphCloner graphCloner, @Assisted String resultFilesSuffix) {
		super(graph, egoNode, graphCloner);
		this.ss = resultFilesSuffix;
	}
	
	@Override
	protected String getSpecifiedSuffixForOutputFiles() {
		return ss;
	}
	
	public interface GraphstreamAnalyzerSuffixFactory {
		GraphstreamAnalyzerSuffix create(Graph graph, Node node, String suffix);
	}
}
