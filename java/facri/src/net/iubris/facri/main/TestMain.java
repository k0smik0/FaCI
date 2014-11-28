package net.iubris.facri.main;

import java.util.Random;
import java.util.function.IntConsumer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;

public class TestMain {
	public static void main(String[] args) {
		Graph graph = new SingleGraph( "test", false, true );
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
////		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.enableAutoLayout();
		graph.display();
		graph.addNode("1");
//		Random random = new Random();
//		int i=0;
//		while(i<100) {
//				int a = random.nextInt();
//				int b = a/random.nextInt();
//				graph.addEdge(a+"_"+b, ""+a, ""+b);
//				i++;
//		}

//		graph.addEdge( "AB", "A", "B" );
//		graph.addEdge( "BC", "B", "C" );
//		graph.addEdge( "CA", "C", "A" );
		
		System.out.println(graph.getNodeCount()+" "+graph.getEdgeCount());
		
	}
}

