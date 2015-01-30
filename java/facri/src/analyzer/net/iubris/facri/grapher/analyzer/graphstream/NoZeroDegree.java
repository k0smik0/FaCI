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
