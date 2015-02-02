/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphstreamAnalyzerWithEgo.java) is part of facri.
 * 
 *     GraphstreamAnalyzerWithEgo.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphstreamAnalyzerWithEgo.java is distributed in the hope that it will be useful,
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


public class GraphstreamAnalyzerWithEgo extends AbstractGraphstreamAnalyzer {
	
// too bad, but it works and I don't have too much time
	@Override
	protected Graph buildGraph() {
		Graph graph = 
					super.graphCopyDataHolder.getGraph();
		int removed = NoZeroDegree.removeZeroDegreeNodes(this.graph);
		Printer.println("Dichotomized "+this.graph.getId()+"removing node with degree=0: removed "+removed+" nodes.");
		return graph;
	}
	
	private final String ss = "";
	
	@AssistedInject
	public GraphstreamAnalyzerWithEgo(@Assisted Graph graph, @Assisted Node egoNode, GraphCloner graphCloner) {
		super(graph, egoNode, graphCloner);	
	}
	
	@Override
	protected String buildSpecifiedSuffixForOutputFiles() {
		return ss;
	}
}
