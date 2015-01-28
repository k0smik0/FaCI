package net.iubris.facri.main;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.IntConsumer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class TestMain {
	protected static Map<Integer,Node> nodes= new ConcurrentSkipListMap<>();

	public static void main(String[] args) {
		Graph graph = new SingleGraph( "test", false, true );
		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
////		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.enableAutoLayout();
		graph.display();
//		graph.addNode("1");
		Random random = new Random();
//		int i=0;
		System.out.println(Integer.MAX_VALUE-Integer.MIN_VALUE);
		random.ints(1, 1000).limit(100).forEach( new IntConsumer() {
			@Override
			public void accept(int value) {
				int a = value;
				float normalizedA = ((float)a-1)/(1000-1);
//				System.out.println(a+" "+normalizedA);
//				System.out.println(b+" "+normalizedB);
				Node nodeA = graph.addNode(""+a);
				nodes.put(a,nodeA);
				nodeA.setAttribute("ui.size", normalizedA*20);
				nodeA.setAttribute("ui.class", "ego");
				nodeA.setAttribute("ui.color", normalizedA);
				pause(100);
				

				int b = a/value;
				float normalizedB = ((float)b-1)/(1000-1);
				Node nodeB = graph.addNode(""+b);
				nodes.put(b,nodeB);
				nodeB.setAttribute("ui.size", normalizedB*20);
				nodeB.setAttribute("ui.class", "ego");
				nodeB.setAttribute("ui.color", normalizedB);
				
//				random.ints(1, 100).limit(2).parallel().forEach( new IntConsumer() {
//					@Override
//					public void accept(int value) {
//						graph.addEdge(a+"_"+b, ""+a, ""+b);
//					}
//				});
				
			}
		});
		
		int i=0;
		while(i<10) {
		for (Node node : nodes.values()) {
			int nextInt = random.nextInt(1000);
			if (nodes.containsKey(nextInt)) {
				Node node2 = nodes.get(nextInt);
				graph.addEdge(node.getId()+"_"+node2.getId(), node, node2);
			}
		}
		i++;
		}
		
		/*random.ints(1, 1000).limit(70).forEach( new IntConsumer() {
			@Override
			public void accept(int value) {
				Node nodeA = nodes.get(value);
				Node nodeB = nodes.get(random.nextInt(1000));
				if (nodeA!=null && nodeB!=null)
					graph.addEdge(nodeA.getId()+"_"+nodeB.getId(), nodeA, nodeB); 
			}
		});*/

		System.out.println(graph.getNodeCount()+" "+graph.getEdgeCount());
	}
	
	static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} 
}

