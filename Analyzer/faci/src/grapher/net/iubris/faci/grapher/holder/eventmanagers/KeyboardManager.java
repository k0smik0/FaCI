/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (KeyboardManager.java) is part of facri.
 * 
 *     KeyboardManager.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     KeyboardManager.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.holder.eventmanagers;

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
