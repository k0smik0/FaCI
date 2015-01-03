package net.iubris.facri.model.graph;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.parsers.DataParser;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.Camera;
import org.graphstream.ui.swingViewer.util.DefaultMouseManager;
import org.graphstream.ui.swingViewer.util.DefaultShortcutManager;

@Singleton
public class GraphsHolder {
	
	private final Graph friendshipsGraph;
	private final Graph interactionsGraph;
	private final Viewer friendshipsGraphViewer;
	private final Viewer interactionsGraphViewer;
//	private ViewerPipe fromViewerInteractions;
	private final World world;
	private final DataParser dataParser;
	private boolean interactionsGraphCreated = false;
	private boolean friendshipsGraphCreated = false;
	
	/*private class NodeViewerListener implements ViewerListener {
		private final Graph graph;
		private LinkedBlockingQueue<String> queue;
//		private boolean loop = true;
		
		public NodeViewerListener(Graph graph, LinkedBlockingQueue<String> queue) {
			this.graph = graph;
			this.queue = queue;
		}
		@Override
		public void viewClosed(String viewName) {
//			loop = false;
		}
		@Override
		public void buttonReleased(String id) {}
		@Override
		public void buttonPushed(String id) {
			System.out.println(id+": "+graph.getNode(id).getDegree());
			queue.add(id);
		}
//		public void doLoop() {
//			loop = true;
//		}
	};*/
	
	// TODO: refactor constructor
	@Inject
	public GraphsHolder(World world, DataParser dataParser) {
		this.world = world;
		this.dataParser = dataParser;
		this.friendshipsGraph = new MultiGraph("Friendships",false,true);
		this.friendshipsGraphViewer = prepareForDisplay(friendshipsGraph);
		this.interactionsGraph = new MultiGraph("Interactions",false,true);
		this.interactionsGraphViewer = prepareForDisplay(interactionsGraph);
		
//		System.out.print("[");
		System.setProperty("sun.java2d.opengl", "True");
//		System.out.println("]");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		System.out.print("\n");
	}
	
	public World getWorld() {
		return world;
	}
	
	public void reparseCSS() {
		if (friendshipsGraph!=null) {
			friendshipsGraph.setAttribute("ui.stylesheet", "");
			friendshipsGraph.setAttribute("ui.stylesheet", "url('friendships.css')");
//			Viewer viewer = graph.display();
//			viewer.enableAutoLayout();
		}
		if (interactionsGraph!=null) {
			interactionsGraph.setAttribute("ui.stylesheet", "");
			interactionsGraph.setAttribute("ui.stylesheet", "url('interactions.css')");
		}
	}
	
	private Viewer prepareForDisplay(Graph graph) {
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
//		graph.addAttribute("ui.speed");
		
//		graph.display();

//		System.out.println("graph: "+graph);
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//		System.out.println("viewer: "+viewer);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		viewer.enableAutoLayout();
		
		return viewer;
	}
	
	
	public void prepareForDisplayFriendships() {
//		friendshipsGraphViewer.close();
		View view = friendshipsGraphViewer.getDefaultView();
		if (view==null) {
			view = friendshipsGraphViewer.addDefaultView(true);
		}
		view.setVisible(true);
		
		view.setMouseManager(new InternalMouseManager(friendshipsGraphViewer));
		view.setShortcutManager(new InternalShortcutManager(friendshipsGraphViewer));
	}
	
	public void prepareForDisplayInteractions() {
		View view = interactionsGraphViewer.getDefaultView();
		if (view==null) {
			view = interactionsGraphViewer.addDefaultView(true);
		}
		view.setVisible(true);
		
		view.setMouseManager(new InternalMouseManager(interactionsGraphViewer));
		view.setShortcutManager(new InternalShortcutManager(interactionsGraphViewer));
	}
	
	public Viewer getInteractionsGraphViewer() {
		return interactionsGraphViewer;
	}
	public Viewer getFriendshipsGraphViewer() {
		return friendshipsGraphViewer;
	}
	
	class InternalMouseManager extends DefaultMouseManager {
		private final Viewer viewer;
//		private double zoom;
//		private Camera camera;
		
//		boolean focusOnClick = true;
		
		public InternalMouseManager(Viewer viewer) {
			this.viewer = viewer;
//			camera = viewer.getDefaultView().getCamera();
//			zoom = viewer.getDefaultView().getCamera().getViewPercent();
		}
		
		
		
//		@Override
		protected void mouseButtonRelease(MouseEvent event, ArrayList<GraphicElement> elements) {
			super.mouseButtonRelease(event, elements);
//			System.out.println(  event.getComponent() );
//			
//			if (event.isControlDown())
//				elements.parallelStream()
//	//				.forEach(e->e.setAttribute("ui.class", "nottexted");System.out.println(););
//				.forEach( new Consumer<GraphicElement>() {
//					@Override
//					public void accept(GraphicElement e) {
//	//					e.getAttributeKeySet();
//						e.setAttribute("ui.class", "nottexted");
//						System.out.println(e.getAttribute("ui.class"));
//						
//					}
//				});
			elements
					.parallelStream()
					.forEach(e -> e.removeAttribute("ui.label"));
		}
		
//		@Override
//		public void mouseEntered(MouseEvent event) {
//			super.mouseEntered(event);
////			camera.setViewPercent(zoom);
//		}
		

		@Override
		protected void mouseButtonPressOnElement(GraphicElement element, MouseEvent event) {
			super.mouseButtonPressOnElement(element, event);
 
			// clicked node with ctrl+shift+alt+meta
			if (event.getButton()==1 && event.isControlDown() && event.isAltDown() && event.isShiftDown() /*&& event.isMetaDown()*/ && element instanceof GraphicNode) {
//				element.addAttribute("ui.label",element.getId());
//				element.addAttribute("ui.class","marked");
//				System.out.println("button 1");
//				System.out.println("\n"+element.getAttribute("ui.label"));
//				System.out.println("\n"+element.getAttribute("ui.marked"));
				
//				element.setAttribute("ui.label",element.getId());
				
				if (!world.isParsingDone()) {
					System.out.println("Graphs was generated from cache and there are no data for user, so parsing...");
					try {
						dataParser.parse();
					} catch (JAXBException | XMLStreamException | IOException e) {
						e.printStackTrace();
					}
					System.out.println("done.");
				}
				
				String uid = element.getId();
//				System.out.println(uid);
				
				Optional<Ego> searchMe = world.searchMe(uid);
				
				if (searchMe.isPresent())
					System.out.println(searchMe.get());
				else {
					world.searchUserById(uid).ifPresent(u->System.out.println(u)); 
				}
				return;
			}
			
			
			
			if (event.getButton()==1 && event.isShiftDown() && /*focusOnClick &&*/ element instanceof GraphicNode) {
				centerOnNode(element, viewer);
				
				String nodeId = element.getId();
				showDialog(viewer, nodeId, view);
				
				return;
			}
			
			if (event.getButton()==3 && element instanceof GraphicNode) {
				element.removeAttribute("ui.label");
				element.removeAttribute("ui.marked");
				return;
			}
		}
	}
	

	public void setInteractionsGraphsCreated(boolean isCreated) {
		interactionsGraphCreated = isCreated;
	}
	public boolean isInteractionsGraphsCreated() {
		return interactionsGraphCreated;
	}
	public void setFriendshipsGraphsCreated(boolean isCreated) {
		friendshipsGraphCreated = isCreated;
	}
	public boolean isFriendshipsGraphCreated() {
		return friendshipsGraphCreated;
	}
	
//	public void hideInteractionsGraph() {
//		interactionsGraphViewer.getDefaultView().setVisible(false);
////		interactionsGraph.
//	}

	public Graph getInteractionsGraph() {
		return interactionsGraph;
	}
	
	/*public ViewerPipe getViewerPipeInteractions(LinkedBlockingQueue<String> queue) {
		fromViewerInteractions = interactionsGraphViewer.newViewerPipe();
//		fromViewerInteractions.addViewerListener( new NodeViewerListener(interactionsGraph, queue) );
      fromViewerInteractions.addSink(interactionsGraph);
		return fromViewerInteractions;
	}*/
	
	public Graph getFriendshipsGraph() {
		return friendshipsGraph;
	}
	
	
	private void launchUrl(String urlToLaunch) {
       try {
           if ( !Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported( java.awt.Desktop.Action.BROWSE) ){
               JOptionPane.showMessageDialog(null, "On this computer java cannot open automatically url in browser, you have to copy/paste it manually.");
               return;
           }
           
           Desktop desktop = Desktop.getDesktop();
           URI uri = new URI(urlToLaunch);
           
           desktop.browse(uri);
       } catch (URISyntaxException ex) {
           JOptionPane.showMessageDialog(null, "Url ["  + urlToLaunch + "] seems to be invalid ");
       } catch (IOException ex) {
           JOptionPane.showMessageDialog(null, "There was some error opening the url. \n Details:\n" + ex.getMessage());
       }       
   }
	
	
	private void centerOnNode(GraphicElement element, Viewer viewer) {
		double x = element.getX();
		double y = element.getY();
		double z = element.getZ();

		Camera camera = viewer.getDefaultView().getCamera();
		camera.setViewCenter(x, y, z);
	}
	private void showDialog(Viewer viewer, String nodeId, View view) {
		String profileUrl = "https://www.facebook.com/profile.php?id="+nodeId;
		Node node = viewer.getGraphicGraph().getNode(nodeId);
		String nodeInfo = "in-degree: "+node.getInDegree()
				+"<br/>out-degree: "+node.getOutDegree()
				+"<br/>url: <a href='"+profileUrl+"'>"+profileUrl+"</a>";
		JEditorPane editorPane = new JEditorPane("text/html", nodeInfo);
		buildJEditorPane(editorPane);
		JOptionPane.showMessageDialog(view, 
//				"You click on " + element.getId()
				editorPane
				);
	}
	
	private void buildJEditorPane(JEditorPane ep) {
		ep.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					if (e.getURL() == null) {
						// System.out.println("e.getURL: is NULL");
						JOptionPane.showMessageDialog(null,
								"The text was clicked but hyperlink seems to contain invalid url and thus is NULL");
					}
					else {
						// System.out.println("e.getURL: " + e.getURL());
						launchUrl(e.getURL().toString()); // roll your own link launcher or use Desktop if J6+
					}
				}
			}
		});
		ep.setEditable(false);
	}
	
	class InternalShortcutManager extends DefaultShortcutManager {
		public InternalShortcutManager(Viewer viewer) {
		}
		@Override
		public void keyTyped(KeyEvent event) {
			super.keyTyped(event);
			switch(event.getKeyChar()) {
			case 'c':
//				GraphicGraph graphicGraph = viewer.getGraphicGraph();
//				Iterator<Node> iterator = graphicGraph.iterator();
//				if (iterator.hasNext()) {
//					Node next = iterator.next();
//					viewer
//						.getDefaultView()
//						.getCamera().setViewCenter(next.get, y, z);
//				}
				break;
			default:
				break;
			}
		}
	}
	
}
