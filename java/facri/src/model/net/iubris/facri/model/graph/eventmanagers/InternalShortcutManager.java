package net.iubris.facri.model.graph.eventmanagers;

import java.awt.event.KeyEvent;

import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.util.DefaultShortcutManager;

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