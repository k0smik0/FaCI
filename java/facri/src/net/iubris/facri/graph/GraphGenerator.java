package net.iubris.facri.graph;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GraphGenerator {
	
	private final GraphModel graphModel;
	private final Workspace workspace;
	private Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private Map<String, Edge> myFriendsEdgesMap = new ConcurrentHashMap<>();

	public GraphGenerator() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
      pc.newProject();
      workspace = pc.getCurrentWorkspace();

      //Get a graph model - it exists because we have a workspace
      graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	}
	
	public void generate(World world) {
		
		Ego ego = world.getMyUser();
		String egoId = ego.getId();
		
		Node egoNode = graphModel.factory().newNode(egoId);
      egoNode.getNodeData().setLabel(egoId);
      
      Iterator<Entry<String, FriendOrAlike>> myFriendsIterator = world.getMyFriendsMap().entrySet().iterator();
      while (myFriendsIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendEntry = myFriendsIterator.next();
      	String friendId = myFriendEntry.getKey();
      	
      	// if contains, this stuff has already done
      	if (!myFriendsNodesMap.containsKey(friendId)) {
      	
      		Node friendNode = createNode(friendId);
	         myFriendsNodesMap.put(friendId, friendNode );
	         Edge myFriendNodeEdge = graphModel.factory().newEdge(egoNode, friendNode, 1f, false);
	         myFriendsEdgesMap.put(friendId, myFriendNodeEdge);
	         
//	         FriendOrAlike myFriend = myFriendEntry.getValue();
//	         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
	         
//	         for (String mutualFriendsId : mutualFriendsIds) {
//					if (!friendsNodeMap.containsKey(mutualFriendsId)) {
//						friendsNodeMap.put()
//					} else {
//						friendsNodeMap.put
//					}
//				}
         
      	}
      	
      }
		
	}
	
	private Node createNode(String id) {
		Node node = graphModel.factory().newNode(id);
   	node.getNodeData().setLabel(id);
   	return node;
	}

}
