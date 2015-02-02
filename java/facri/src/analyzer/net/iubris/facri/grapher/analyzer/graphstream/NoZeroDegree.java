/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (NoZeroDegree.java) is part of facri.
 * 
 *     NoZeroDegree.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     NoZeroDegree.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.grapher.analyzer.graphstream;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class NoZeroDegree {

	public static int removeZeroDegreeNodes(Graph graph) {
		AtomicInteger removed = new AtomicInteger(0);
		Stream<Node> stream = StreamSupport.stream(graph.spliterator(), true);
		stream.forEach(node -> {
			if (node.getDegree()==0) {
				graph.removeNode(node);
				removed.incrementAndGet();
			}
		});
		/*Iterator<Node> iterator = graph.iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (node.getDegree()==0) {
//				try {
					iterator.remove();
					removed.incrementAndGet();
//				} catch(NullPointerException npe) {}
			}
		}*/
		return removed.get();
	}
}
