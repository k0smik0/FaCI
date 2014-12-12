package net.iubris.facri.graph.generators.gephi;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import net.iubris.facri.graph.generators.FriendshipsGraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GephiFriendshipsGraphGenerator implements FriendshipsGraphGenerator {
	
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
	

	@Inject
	public GephiFriendshipsGraphGenerator(UndirectedGraph undirectedGraph, GraphFactory graphFactory, World world) {
      this.graphFactory = graphFactory;
      this.undirectedGraph = undirectedGraph;
		this.world = world;
	}
	
	@Override
	public void generateMeWithMyFriendsAndTheirFriends() {
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
		createFriendsOfFriends();
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
		createFriendsOfFriends();
	}
	
	private Node createMe() {
		Ego ego = world.getMyUser();
		String egoId = ego.getId();
      this.egoNode = createNode(egoId);
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
	
	private void createFriendsOfFriends() {
		Iterator<Entry<String, FriendOrAlike>> friendsOfMyFriendsIterator = world.getOtherUsersMap().entrySet().iterator();
		while (friendsOfMyFriendsIterator.hasNext()) {
			Entry<String, FriendOrAlike> friendOfMyFriendEntry = friendsOfMyFriendsIterator.next();
			String friendOfMyFriendId = friendOfMyFriendEntry.getKey();

			FriendOrAlike friendOfMyFriend = friendOfMyFriendEntry.getValue();
			Set<String> friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds = friendOfMyFriend.getMutualFriends();

			Node friendOfMyFriendNode = createNode(friendOfMyFriendId);
			friendOfFriendNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

			for (String friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds) {
				Node myFriendNode = getOrCreateFriendNode(friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId);
				
				Edge friendOfFriendWithMyFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode);
				friendsOfFriendsWithFriendsTable.put(friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, friendOfFriendWithMyFriendEdge);
			}
		}
	}
	
	private Node createNode(String id) {
		Node node = graphFactory.newNode(id);
   	node.getNodeData().setLabel(id);
   	undirectedGraph.addNode(node);
   	return node;
	}
	
	private Edge createEdge(Node firstNode, Node secondNode) {
		Edge createdEdge = graphFactory.newEdge(firstNode, secondNode/*, 1f, true*/);
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
	}

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
	

	public void clear() {
		egoNode = null;
		myFriendsNodesMap.clear();
		myFriendsWithMeEdgesMap.clear();
		graphNodesCount=0;
		graphEdgesCount=0;
		
		undirectedGraph.clear();
		myFriendsToMutualFriendsTable.clear();
	}
	
	
}
