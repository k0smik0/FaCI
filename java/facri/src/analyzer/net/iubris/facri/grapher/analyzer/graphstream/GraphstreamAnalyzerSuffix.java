/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphstreamAnalyzerSuffix.java) is part of facri.
 * 
 *     GraphstreamAnalyzerSuffix.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphstreamAnalyzerSuffix.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
