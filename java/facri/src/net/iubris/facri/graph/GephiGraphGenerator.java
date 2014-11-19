package net.iubris.facri.graph;


import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GephiGraphGenerator {
	
	private final Workspace workspace;
	private final GraphFactory graphFactory;
	private final DirectedGraph directedGraph;
	
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Map<String, Edge> myFriendsWithMeEdgesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> myFriendsToMutualFriendsTable = HashBasedTable.create();
	
	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsOfFriendsWithFriendsTable = HashBasedTable.create();

	@javax.inject.Inject
	public GephiGraphGenerator(Workspace workspace, DirectedGraph directedGraph, GraphFactory graphFactory) {
//		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
//      pc.newProject();
//      workspace = pc.getCurrentWorkspace();
      this.workspace = workspace;

      //Get a graph model - it exists because we have a workspace
//      graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
//      this.graphModel = graphModel;
//      graphFactory = graphModel.factory();
      this.graphFactory = graphFactory;
      
//      directedGraph = graphModel.getDirectedGraph();
      this.directedGraph = directedGraph;
	}
	
	public void generate(World world) {
		
		Node egoNode = createMe(world);
		createMyFriends(world, egoNode, true);
      
		createFriendsOfFriends(world);
	}
	
	private Node createMe(World world) {
		Ego ego = world.getMyUser();
		String egoId = ego.getId();
		
      Node egoNode = createNode(egoId);
      
      return egoNode;
	}
	
	private void createMyFriends(World world, Node egoNode, boolean includeMyUserNode) {
		Iterator<Entry<String, FriendOrAlike>> myFriendsIterator = world.getMyFriendsMap().entrySet().iterator();
      while (myFriendsIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendEntry = myFriendsIterator.next();
      	String friendId = myFriendEntry.getKey();

      	// include me
      	Node myFriendNode = null;
      	if (includeMyUserNode)
      		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(friendId, egoNode);
      	else
      		myFriendNode = createNode(friendId);
	         
         FriendOrAlike myFriend = myFriendEntry.getValue();
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
         
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         	
         	if (areMutualFriendsAlreadyComputed(friendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
//         	if ( myFriendsToMutualFriendsTable.contains(friendId, myOtherFriendAlsoMutualWithMeId) || myFriendsToMutualFriendsTable.contains(myOtherFriendAlsoMutualWithMeId, friendId) ) {
//         		continue;
//         	}
         	
         	Node myOtherFriendAlsoMutualNode = null;
         	// this creation below could be decrease outer while iterations ?
         	if (includeMyUserNode)
         		myOtherFriendAlsoMutualNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(myOtherFriendAlsoMutualWithMeId, egoNode);
         	else
         		myOtherFriendAlsoMutualNode = createNode(myOtherFriendAlsoMutualWithMeId);
         	
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode);         	
         	myFriendsToMutualFriendsTable.put(friendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);         	
			}
      }
	}
	
	private void createFriendsOfFriends(World world) {
//    System.out.println("\n"+world.getOtherUsersMap().size()+"\n");
		Iterator<Entry<String, FriendOrAlike>> myFriendsOfFriendsIterator = world.getOtherUsersMap().entrySet().iterator();
		while (myFriendsOfFriendsIterator.hasNext()) {
			Entry<String, FriendOrAlike> myFriendOfFriendEntry = myFriendsOfFriendsIterator.next();
			String friendOfFriendId = myFriendOfFriendEntry.getKey();

			FriendOrAlike myFriendOfFriend = myFriendOfFriendEntry.getValue();
			Set<String> friendOfFriendMutualFriendsIdsThatIsMyFriendsIds = myFriendOfFriend.getMutualFriends();

			Node friendOfFriendNode = createNode(friendOfFriendId);
			friendOfFriendNodeMap.put(friendOfFriendId, friendOfFriendNode);

			for (String friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId: friendOfFriendMutualFriendsIdsThatIsMyFriendsIds) {
				if (areFriendsOfFriendsAlreadyComputed(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfFriendId)) {
//					System.out.println("no");
         		continue;
				}
				Node myFriendNode = myFriendsNodesMap.get(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				Edge friendOfFriendWithMyFriendEdge = createEdge(myFriendNode, friendOfFriendNode);
				friendsOfFriendsWithFriendsTable.put(friendOfFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfFriendWithMyFriendEdge);
			}
		}
	}
	
	private Node createNode(String id) {
		Node node = graphFactory.newNode(id);
   	node.getNodeData().setLabel(id);
   	directedGraph.addNode(node);
   	return node;
	}
	
	private Edge createEdge(Node firstNode, Node secondNode) {
		Edge createdEdge = graphFactory.newEdge(firstNode, secondNode, 1f, true);
   	directedGraph.addEdge(createdEdge);
   	return createdEdge;
	}
	
	private boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( myFriendsToMutualFriendsTable.contains(firstUserId, secondUserId) || myFriendsToMutualFriendsTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}
	private boolean areFriendsOfFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( friendsOfFriendsWithFriendsTable.contains(firstUserId, secondUserId) || friendsOfFriendsWithFriendsTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}
	
	public void testGraph() {
		System.out.println( "my friends nodes: "+ myFriendsNodesMap.size()+"\n" 
				+"my friends with me edges: "+myFriendsWithMeEdgesMap.size()+"\n"
				+"my friends to each others edges: "+myFriendsToMutualFriendsTable.values().size()
				+"\n"
				+"friends of my friends nodes: "+ friendOfFriendNodeMap.size()+"\n"
				+"friends of my friends to they/my friends edges: "+friendsOfFriendsWithFriendsTable.size()+"\n"
		);
	}
	
	public void exportGraphToGraphML(String fileName) {
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		Exporter exporterGraphML = ec.getExporter("graphml");    //Get GraphML exporter
		exporterGraphML.setWorkspace(workspace);
		StringWriter stringWriter = new StringWriter();
		ec.exportWriter(stringWriter, (CharacterExporter) exporterGraphML);
		try {
			FileWriter f=  new FileWriter("facri_tmp.graphml");			
			ec.exportWriter(f, (CharacterExporter) exporterGraphML);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(String friendId, Node egoNode) {
		if (!myFriendsNodesMap.containsKey(friendId)) {
			
			Node friendNode = createNode(friendId);
	      myFriendsNodesMap.put(friendId, friendNode );

	      Edge myFriendNodeEdge = createEdge(egoNode, friendNode);
	      myFriendsWithMeEdgesMap.put(friendId, myFriendNodeEdge);
	      
	      return friendNode;
		}
		return myFriendsNodesMap.get(friendId);
	}
}
