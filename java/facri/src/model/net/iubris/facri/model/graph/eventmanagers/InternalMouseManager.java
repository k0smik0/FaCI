package net.iubris.facri.model.graph.eventmanagers;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.iubris.facri.model.world.World;
import net.iubris.facri.utils.Printer;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.Camera;
import org.graphstream.ui.swingViewer.util.DefaultMouseManager;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class InternalMouseManager extends DefaultMouseManager {

	private final Viewer viewer;
	private final World world;
//		private double zoom;
//		private Camera camera;
	
//		boolean focusOnClick = true;
	
	@AssistedInject
	public InternalMouseManager(@Assisted Viewer viewer, World world) {
		this.viewer = viewer;
//			camera = viewer.getDefaultView().getCamera();
//			zoom = viewer.getDefaultView().getCamera().getViewPercent();
		this.world = world;
	}
	
	@Override
	protected void mouseButtonPress(MouseEvent mouseEvent) {
		super.mouseButtonPress(mouseEvent);
		if (mouseEvent.isControlDown()) {
//				viewer.getDefaultView().repaint();
			view.getCamera().resetView();
		}
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
			
			/*if (!world.isParsingDone()) {
				Printer.println("Graphs was generated from cache and there are no data for user, so parsing...");
				try {
					dataParser.parse();
				} catch (JAXBException | XMLStreamException | IOException e) {
					e.printStackTrace();
				}
				Printer.println("done.");
			}*/
			
			String uid = element.getId();
			
//			Optional<Ego> searchMe = world.searchMe(uid);
//			if (searchMe.isPresent())
//				Printer.println(searchMe.get());
//			else
			world.searchMe(uid).ifPresent(m->{ Printer.println(m); return; });
			world.searchUserById(uid).ifPresent(u->{ Printer.println(u); return; }); 
			
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
	
	
	void centerOnNode(GraphicElement element, Viewer viewer) {
		double x = element.getX();
		double y = element.getY();
		double z = element.getZ();

		Camera camera = viewer.getDefaultView().getCamera();
		camera.setViewCenter(x, y, z);
	}
	void showDialog(Viewer viewer, String nodeId, View view) {
		String profileUrl = "https://www.facebook.com/profile.php?id="+nodeId;
		Node node = viewer.getGraphicGraph().getNode(nodeId);
		AtomicDouble enteringInteractions = new AtomicDouble(0);
		AtomicInteger enteringInteractionsCounter = new AtomicInteger(0); 
//		node.getEachEnteringEdge()
//			.forEach(e->{
//				if (e.isDirected()) {
//					float interactions = e.getAttribute("interactions");
//					enteringInteractions.addAndGet(interactions);
//					enteringInteractionsCounter.incrementAndGet();
//				}
//			});
		AtomicDouble leavingInteractions = new AtomicDouble(0);
		AtomicInteger leavingInteractionsCounter = new AtomicInteger(0);
//		node.getEachLeavingEdge()
//			.forEach(e->{
//				if (e.isDirected()) {
//					float interactions = e.getAttribute("interactions");
//					leavingInteractions.addAndGet(interactions);
//					leavingInteractionsCounter.incrementAndGet();
//				}
//			});
		
		Iterable<Edge> eachEdge = node.getEachEdge();
		eachEdge.forEach(e->{
			if (e.getSourceNode().getId().equals(nodeId)) { // leaving
				float interactions = e.getAttribute("interactions");
				leavingInteractions.addAndGet(interactions);
				leavingInteractionsCounter.incrementAndGet();
			} else if (e.getTargetNode().getId().equals(nodeId)) { // entering
				float interactions = e.getAttribute("interactions");
				enteringInteractions.addAndGet(interactions);
				enteringInteractionsCounter.incrementAndGet();
			} else {
				System.out.println(e.getId()+" ?!");
			}
		});
		
//		Node egoNode = graph.getNode(world.getMyUser().getUid());
//		System.out.println(egoNode.getInDegree());
		
		float eValue = enteringInteractions.floatValue();
		float lValue = leavingInteractions.floatValue();
		String nodeInfo = "in-degree: "+(node.getInDegree()*eValue)+" ("+enteringInteractionsCounter.get()+"*"+eValue+")"
				+"<br/>out-degree: "+(node.getOutDegree()*lValue)+" ("+leavingInteractionsCounter.get()+"*"+lValue+")"
				+"<br/>url: <a href='"+profileUrl+"'>"+profileUrl+"</a>";
		JEditorPane editorPane = new JEditorPane("text/html", nodeInfo);
		buildJEditorPane(editorPane);
		JOptionPane.showMessageDialog(view, editorPane);
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
	
	public static interface InternalMouseManagerFactory {
		InternalMouseManager create(Viewer viewer);
	}
}