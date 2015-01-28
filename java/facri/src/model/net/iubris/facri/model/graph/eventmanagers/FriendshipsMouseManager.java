package net.iubris.facri.model.graph.eventmanagers;

import net.iubris.facri.model.world.World;

import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class FriendshipsMouseManager extends MouseManager {

	@AssistedInject
	public FriendshipsMouseManager(@Assisted Viewer viewer, World world) {
		super(viewer, world);
	}
	
	@Override
	protected String buildNodeInfo(Node node) {
		String nodeId = node.getId();
		String profileUrl = profileUrlPrefix+nodeId;
		String nodeInfo = 
				"degree: "+node.getDegree()
				+"<br/>url: <a href='"+profileUrl+"'>"+profileUrl+"</a>";
		return nodeInfo;
	}
	
	public interface FriendshipsMouseManagerFactory /*extends MouseManagerFactory<FriendshipsMouseManager>*/ {
		FriendshipsMouseManager create(Viewer viewer);
	}

}
