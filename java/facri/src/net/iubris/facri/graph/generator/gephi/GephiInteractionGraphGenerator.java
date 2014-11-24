package net.iubris.facri.graph.generator.gephi;


import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import net.iubris.facri.graph.generator.FriendnessGraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeData;
import org.gephi.graph.api.UndirectedGraph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GephiInteractionGraphGenerator implements FriendnessGraphGenerator {
	
	private final GraphFactory graphFactory;
	private final UndirectedGraph undirectedGraph;
	
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Map<String, Edge> myFriendsWithMeEdgesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> myFriendsToMutualFriendsTable = HashBasedTable.create();
	
	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsOfFriendsWithFriendsTable = HashBasedTable.create();
	
	private final World world;

	private Node egoNode;
	
	private int graphNodesCount;
	private int graphEdgesCount;
	private final int appreciationsDenominator;
	private final int appreciationsMin;
	

	@Inject
	public GephiInteractionGraphGenerator(UndirectedGraph undirectedGraph, GraphFactory graphFactory, World world) {
      this.graphFactory = graphFactory;
      this.undirectedGraph = undirectedGraph;
		this.world = world;
		Set<Integer> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = Collections.min(appreciationsRange);
		appreciationsDenominator = Collections.max(appreciationsRange)-appreciationsMin;		
	}
	
	@Override
	public void generateAll() {
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
		//TODO
//		createFriendsOfFriends();
	}
	
	@Override
	public void generateMyFriends() {
		createMyFriendsOnly();
	}
	
	@Override
	public void generateMeWithMyFriends() {
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		createMyFriendsOnly();
		//TODO
//		createFriendsOfFriends();
	}
	
	private Node createMe() {
		Ego ego = world.getMyUser();
      this.egoNode = createNode(ego);
      return egoNode;
	}
	
	/**
	 * create my friends graph not including my node
	 */
	private void createMyFriendsOnly() {
		createMyFriends(false);
	}
	
	/**
	 * create my friends graph including my node and relative edges
	 * @param egoNode
	 */
	void createMyFriendsWithMe(Node egoNode) {
		createMyFriends(true);
	}
	
	private void createMyFriends(boolean includeMyUserNode) {
		Map<String, FriendOrAlike> myFriendsMap = world.getMyFriendsMap();
		Iterator<Entry<String, FriendOrAlike>> myFriendsMapIterator = myFriendsMap.entrySet().iterator();
      while (myFriendsMapIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendEntry = myFriendsMapIterator.next();
      	String myFriendId = myFriendEntry.getKey();

      	// include me
      	Node myFriendNode = null;
      	if (includeMyUserNode)
      		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(myFriendId, egoNode);
      	else {
      		myFriendNode = createAndMaintainOrRetrieveFriendNode(myFriendId); 
      	}
	         
         FriendOrAlike myFriend = myFriendEntry.getValue();
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
         	
         
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         	
         	// that is: a cell in myFriendsToMutualFriendsTable contains edge between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
         	
         	// this creation below could be decrease outer while iterations ?
         	Node myOtherFriendAlsoMutualNode = createAndMaintainOrRetrieveFriendNode(myOtherFriendAlsoMutualWithMeId);
         	if (includeMyUserNode)
         		createAndMaintainOrRetrieveFriendEdgeWithMe(myOtherFriendAlsoMutualNode , egoNode);
         	
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode);         	
         	myFriendsToMutualFriendsTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);
         	
			}
      }
	}
	
	/*
	private void createFriendsOfFriends() {
		Iterator<Entry<String, FriendOrAlike>> myFriendsOfFriendsIterator = world.getOtherUsersMap().entrySet().iterator();
		while (myFriendsOfFriendsIterator.hasNext()) {
			Entry<String, FriendOrAlike> myFriendOfFriendEntry = myFriendsOfFriendsIterator.next();
			String friendOfFriendId = myFriendOfFriendEntry.getKey();

			FriendOrAlike myFriendOfFriend = myFriendOfFriendEntry.getValue();
			Set<String> friendOfFriendMutualFriendsIdsThatIsMyFriendsIds = myFriendOfFriend.getMutualFriends();

			Node friendOfFriendNode = createNode(friendOfFriendId);
			friendOfFriendNodeMap.put(friendOfFriendId, friendOfFriendNode);

			for (String friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId: friendOfFriendMutualFriendsIdsThatIsMyFriendsIds) {
				Node myFriendNode = getOrCreateFriendNode(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				
				Edge friendOfFriendWithMyFriendEdge = createEdge(myFriendNode, friendOfFriendNode);
				friendsOfFriendsWithFriendsTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfFriendId, friendOfFriendWithMyFriendEdge);
			}
		}
	}*/
	
	private Node createNode(User user) {
		String id = user.getId();
		Node node = graphFactory.newNode(id);
		NodeData nodeData = node.getNodeData();
		nodeData.setLabel(id);
		nodeData.setSize( user.getOwnPostsCount() );
		int appreciation = user.getOwnLikedPostsCount() + user.getOwnPostsResharingCount();
		int normalizedAppreciation = (appreciation-appreciationsMin)/appreciationsDenominator;
		nodeData.setAlpha( normalizedAppreciation );
		
		undirectedGraph.addNode(node);
		return node;
	}
	
	
	/*private Edge createEdge(Node firstNode, Node secondNode) {
		Edge createdEdge = graphFactory.newEdge(firstNode, secondNode, 1f, true);
   	undirectedGraph.addEdge(createdEdge);
   	return createdEdge;
	}
	
	private Node createAndMaintainOrRetrieveFriendNode(String myFriendId) {
		Node myFriendNode = null;
		if (myFriendsNodesMap.containsKey(myFriendId))
			myFriendNode = myFriendsNodesMap.get(myFriendId);
		else {			
			myFriendNode = createNode(myFriendId);
	      myFriendsNodesMap.put(myFriendId, myFriendNode );
		}
		return myFriendNode;
	}
	
	private void createAndMaintainOrRetrieveFriendEdgeWithMe(Node myFriendNode, Node egoNode) {
		Edge myFriendWithMeEdge = createEdge(egoNode, myFriendNode);
      myFriendsWithMeEdgesMap.put(myFriendNode.getNodeData().getId(), myFriendWithMeEdge);
	}
	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(String myFriendId, Node egoNode) {
		if (myFriendsNodesMap.containsKey(myFriendId))
			return myFriendsNodesMap.get(myFriendId);
		else {			
			Node myFriendNode = createNode(myFriendId);
	      myFriendsNodesMap.put(myFriendId, myFriendNode );

	      Edge myFriendWithMeEdge = createEdge(egoNode, myFriendNode);
	      myFriendsWithMeEdgesMap.put(myFriendId, myFriendWithMeEdge);
	      
	      return myFriendNode;
		}		
	}
	
	private Node getOrCreateFriendNode(String friendId) {
		Node myFriendNode = null;
		if (myFriendsNodesMap.containsKey(friendId)) {
			myFriendNode = myFriendsNodesMap.get(friendId);
			if (undirectedGraph.contains(myFriendNode)) {
				undirectedGraph.addNode(myFriendNode);
			}
		} else {
			myFriendNode = createNode(friendId);
			undirectedGraph.addNode(myFriendNode);
			myFriendsNodesMap.put(friendId,myFriendNode);
		}
		return myFriendNode;
	}
	
	private boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( myFriendsToMutualFriendsTable.contains(firstUserId, secondUserId) || myFriendsToMutualFriendsTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}*/

	public void testGraph() {

		graphNodesCount = undirectedGraph.getNodeCount();
		graphEdgesCount = undirectedGraph.getEdgeCount();
		
		System.out.println( "\tmy friends:\n" 
				+"\t\tnodes (map): "+ myFriendsNodesMap.size()+"\n"
				+"\t\tedges to me (map): "+myFriendsWithMeEdgesMap.size()+"\n"
				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
				+"\tfriends of my friends (maps):\n"
				+"\t\tnodes (map): "+friendOfFriendNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
				+"\t\tedges to their/my friends (map): "+friendsOfFriendsWithFriendsTable.size()+"\n"
				+"\n"
				+"\tgraph nodes:"+graphNodesCount+"\n"
				+"\tgraph edges:"+graphEdgesCount+"\n"
		);
	}
	

	
//	private void incrementMyFriendsToMutualFriendsEdges(int edges) {
//		myFriendsToMutualFriendsEdges+=edges;
//	}
	
	public void clear() {
		undirectedGraph.clear();
		egoNode = null;
		myFriendsNodesMap.clear();
		myFriendsWithMeEdgesMap.clear();
		myFriendsToMutualFriendsTable.clear();
		graphNodesCount=0;
		graphEdgesCount=0;
	}
	
	
}
