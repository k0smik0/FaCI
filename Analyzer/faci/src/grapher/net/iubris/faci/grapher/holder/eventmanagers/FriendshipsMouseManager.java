/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendshipsMouseManager.java) is part of facri.
 * 
 *     FriendshipsMouseManager.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendshipsMouseManager.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.holder.eventmanagers;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import net.iubris.faci.model.world.World;

public class FriendshipsMouseManager extends MouseManager {

	@AssistedInject
	public FriendshipsMouseManager(@Assisted Viewer viewer, World world) {
		super(viewer, world);
	}
	
	@Override
	protected String buildNodeInfo(Node node) {
		String nodeId = node.getId();
		String randomForPrivacy = get3Random();
		String profileUrl = profileUrlPrefix+nodeId+randomForPrivacy;
		String nodeInfo = 
				"degree: "+node.getDegree()
				+"<br/>url: <a href='"+profileUrl+"'>"+profileUrl+"</a>";
		return nodeInfo;
	}
	
	public interface FriendshipsMouseManagerFactory /*extends MouseManagerFactory<FriendshipsMouseManager>*/ {
		FriendshipsMouseManager create(Viewer viewer);
	}

}
