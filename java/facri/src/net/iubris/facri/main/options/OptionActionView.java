package net.iubris.facri.main.options;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.graph.analyzer.graphstream.GraphstreamInteractionsAnalyzer;
import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.facri.parsers.DataParser;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;

public class OptionActionView extends AbstractOptionActionParsable {
	
	private final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	@Inject
	public OptionActionView(DataParser dataParser, GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		super(dataParser);
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}

	@Override
	public void execute() throws FileNotFoundException, JAXBException, XMLStreamException {
		super.doParse();
		doGraph();
	}
	
	void doGraph() {
//		ViewerListenerImplementation viewerListener = new ViewerListenerImplementation();
		
		System.out.print("\ngenerating *me with my friends graph*: ");
//		System.out.println("");
		Graph graph = graphstreamInteractionsGraphGenerator.getGraph();
//		graph.addAttribute("ui.quality");
//		graph.addAttribute("ui.antialias");
		graph.display();
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.enableAutoLayout();
//		ViewerPipe viewerPipe = viewer.newViewerPipe();
//		viewerPipe.addViewerListener(viewerListener);
//		viewerPipe.addSink(graph);

		graphstreamInteractionsGraphGenerator.generateMeWithMyFriends();
		System.out.println(" ok.");
		graphstreamInteractionsGraphGenerator.testGraph();
		
//		while(viewerListener.isLooping()) {
//			viewerPipe.pump();
//		}
		
		GraphstreamInteractionsAnalyzer graphstreamInteractionsAnalyzer = new GraphstreamInteractionsAnalyzer(graph, graphstreamInteractionsGraphGenerator.getEgoNode());
		graphstreamInteractionsAnalyzer.analyzeConnected();
		

		
//		BetweennessCentrality betweennessCentrality = new BetweennessCentrality();
//		betweennessCentrality.betweennessCentrality(graph);
		
		
		
//		ViewerPipe fromViewer = viewer.newViewerPipe();
////      fromViewer.addViewerListener(this);
//      fromViewer.addSink(graph);
//      fromViewer.pump();
	}
	
	final class ViewerListenerImplementation implements ViewerListener {
		private boolean loop = true;
		
		public boolean isLooping() {
			return loop;
		}
		
		@Override
		public void viewClosed(String viewName) {
			loop = false;
		}

		@Override
		public void buttonReleased(String id) {
			System.out.println("released: "+id);
		}

		@Override
		public void buttonPushed(String id) {
			System.out.println("clicked: "+id);
		}
	}
}
