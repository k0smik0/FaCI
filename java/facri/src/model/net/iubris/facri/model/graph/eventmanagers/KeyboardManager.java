package net.iubris.facri.model.graph.eventmanagers;

import java.awt.event.KeyEvent;

import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultShortcutManager;

class KeyboardManager extends DefaultShortcutManager {
		public KeyboardManager(Viewer viewer) {
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