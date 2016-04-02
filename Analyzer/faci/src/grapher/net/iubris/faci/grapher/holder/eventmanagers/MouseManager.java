/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (MouseManager.java) is part of facri.
 * 
 *     MouseManager.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MouseManager.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.holder.eventmanagers;

import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.iubris.faci.model.world.World;
import net.iubris.faci.utils.Printer;

import org.graphstream.graph.Node;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicNode;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

public abstract class MouseManager extends DefaultMouseManager {
	
	protected static Random randomGenerator;
	static {
		randomGenerator = new Random();
	}
	protected String get3Random() {
		int nextInt = randomGenerator.nextInt(1000);
		return String.format("%03d", nextInt);
	}

	protected final String profileUrlPrefix = "https://www.facebook.com/profile.php?id=";
	private final Viewer viewer;
	private final World world;
	private final ViewPanel defaultView;
	private float viewPercent;
	
	public MouseManager(Viewer viewer, World world) {
		this.viewer = viewer;
		this.defaultView = viewer.getDefaultView();
		this.world = world;
		
		this.viewPercent = 0f;
	}
	
	@Override
	protected void mouseButtonPress(MouseEvent mouseEvent) {
		super.mouseButtonPress(mouseEvent);
		
		if (onClickWithMouseFirstButtonAndCtrl(mouseEvent)) return;
		if (onClickWithMouseFirstButtonAndShift(mouseEvent)) return;
//		if (onClickWithMouseFirstButtonAndCtrlShift(mouseEvent)) return;		
	}
	
	protected boolean onClickWithMouseFirstButtonAndCtrl(MouseEvent mouseEvent) {
		if (mouseEvent.isControlDown()) {
			Camera camera = view.getCamera();
			viewPercent = 0f;
			camera.setViewPercent(1);
			camera.resetView();
			return true;
		}
		return false;
	}
	
	protected boolean onClickWithMouseFirstButtonAndShift(MouseEvent mouseEvent) {
		if (mouseEvent.isShiftDown()) {
			Camera camera = view.getCamera();
//			camera.setViewCenter(mouseEvent.getX(), mouseEvent.getY(), camera.getViewCenter().z);
			viewPercent+=0.1;
			camera.setViewPercent(viewPercent);
//			Pauser.sleep(50);
//			camera.setViewPercent(0.99);
			return true;
		}
		return false;
	}
//	protected boolean onClickWithMouseFirstButtonAndCtrlShift(MouseEvent mouseEvent) {
//		if (mouseEvent.isShiftDown() && mouseEvent.isControlDown()) {
//		elements
//		.parallelStream()
//		.forEach(e -> e.removeAttribute("ui.label"));
//		}
//	}
	
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
 
			// button 1 and ctrl+shift+alt
		if (onClickElementWithMouseFirstButtonAndKeyboardCtrlAltShift(element, event)) return;
	
		// button 1 and shift
		if (onClickElementWithMouseFirstButtonAndKeyboardShift(element, event)) return;
	
		// button 3
		if (onClickElementWithMouseThirdButton(element, event)) return;
	}
	
	protected boolean onClickElementWithMouseFirstButtonAndKeyboardCtrlAltShift(GraphicElement element, MouseEvent event) {
		// clicked node with first mouse button and keyboard ctrl+shift+alt
		if (event.getButton()==1 && event.isControlDown() && event.isAltDown() && event.isShiftDown() /*&& event.isMetaDown()*/ && element instanceof GraphicNode) {
//					element.addAttribute("ui.label",element.getId());
//					element.addAttribute("ui.class","marked");
//					System.out.println("button 1");
//					System.out.println("\n"+element.getAttribute("ui.label"));
//					System.out.println("\n"+element.getAttribute("ui.marked"));
		
//					element.setAttribute("ui.label",element.getId());
		
			String uid = element.getId();
		
//				Optional<Ego> searchMe = world.searchMe(uid);
//				if (searchMe.isPresent())
//					Printer.println(searchMe.get());
//				else
			world.searchMe(uid).ifPresent(m->{ Printer.println(m); return; });
			System.out.println("here");
			world.searchUserById(uid).ifPresent(u->{ Printer.println(u); return; }); 
			
			return true;
		}
		return false;
	}
	
	protected boolean onClickElementWithMouseFirstButtonAndKeyboardShift(GraphicElement element, MouseEvent event) {
		// clicked node with shift
		if (event.getButton()==1 && event.isShiftDown() && element instanceof GraphicNode) {
			centerOnNode(element, viewer);
			
			String nodeId = element.getId();
			Node node = viewer.getGraphicGraph().getNode(nodeId);
			node.addAttribute("ui.class", "marked");
			node.addAttribute("ui.label", nodeId);
//				showDialog(nodeId, node, view); // old 1.2
			showDialog(nodeId, node, defaultView); // nightly 1.3
			
			viewer.getDefaultView().getCamera().setViewPercent(0.1);
			
			return true;
		}
		return false;
	}
	
	protected boolean onClickElementWithMouseThirdButton(GraphicElement element, MouseEvent event) {
		// clicked node with third mouse button
		if (event.getButton() == 3 && element instanceof GraphicNode) {
			element.removeAttribute("ui.label");
			element.removeAttribute("ui.marked");
			return true;
		}
		return false;
	}
	
	protected void showDialog(String nodeId, Node node, ViewPanel view) {
		String nodeInfo = buildNodeInfo(node);
		JEditorPane editorPane = new JEditorPane("text/html", nodeInfo);
		buildJEditorPane(editorPane);
		JOptionPane.showMessageDialog(view, editorPane);
	}
	protected abstract String buildNodeInfo(Node node);
	
	protected void launchUrl(String urlToLaunch) {
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
	
	
	protected void centerOnNode(GraphicElement element, Viewer viewer) {
		double x = element.getX();
		double y = element.getY();
		double z = element.getZ();
		Camera camera = viewer.getDefaultView().getCamera();
		camera.setViewCenter(x, y, z);
	}

	protected void buildJEditorPane(JEditorPane ep) {
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
	
	public static interface MouseManagerFactory<M extends MouseManager> {
		M create(Viewer viewer);
	}
}
