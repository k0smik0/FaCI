package net.iubris.facri.graph.generator.gephi;


import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import net.iubris.facri.graph.generator.InteractionsGraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeData;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GephiInteractionsGraphGenerator implements InteractionsGraphGenerator {
	
	private final GraphFactory graphFactory;
	private final DirectedGraph directedGraph;
	
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
	private final Table<String, String, Edge> myFriendsToMutualFriendsTable = HashBasedTable.create();
	
	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsOfFriendsWithFriendsTable = HashBasedTable.create();
	
	private final World world;

	private Ego ego;
	private Node egoNode;
	
	private int graphNodesCount;
	private int graphEdgesCount;
	private int appreciationsDenominator;
	private int appreciationsMin;
	private Map<String, FriendOrAlike> myFriendsMap;
	

	@Inject
	public GephiInteractionsGraphGenerator(DirectedGraph directedGraph, GraphFactory graphFactory, World world) {
      this.graphFactory = graphFactory;
      this.directedGraph = directedGraph;
		this.world = world;
		this.myFriendsMap = world.getMyFriendsMap();
	}
	
	private void retrieveAppreciations() {
		Set<Integer> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = 
				Collections.min(appreciationsRange);
//				(int)appreciationsRange.toArray()[0];
		appreciationsDenominator = 
			Collections.max(appreciationsRange)
//				(int)appreciationsRange.toArray()[appreciationsRange.size()-1]
						-appreciationsMin;
	}
	
	@Override
	public void generateMeWithMyFriendsAndTheirFriends() {
		retrieveAppreciations();
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
		createFriendsOfFriends();
	}
	
	@Override
	public void generateMyFriends() {
		retrieveAppreciations();
		createMyFriendsOnly();
	}
	
	@Override
	public void generateMeWithMyFriends() {
		retrieveAppreciations();
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		retrieveAppreciations();
		createMyFriendsOnly();
		createFriendsOfFriends();
	}
	
	private Node createMe() {
		this.ego = world.getMyUser();
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
      	FriendOrAlike myFriend = myFriendEntry.getValue();

      	// include me
      	Node myFriendNode = null;
      	if (includeMyUserNode)
      		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myFriend);
      	else {
      		myFriendNode = createAndMaintainOrRetrieveFriendNode(myFriend); 
      	}
	         
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();        
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         	
//         	// that is: a cell in myFriendsToMutualFriendsTable contains edge between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
         	
         	// this creation below could be decrease outer while iterations ?
         	Node myOtherFriendAlsoMutualNode = createAndMaintainOrRetrieveFriendNode( myFriendsMap.get(myOtherFriendAlsoMutualWithMeId) );
         	if (includeMyUserNode) {
         		
         		// my mutual friend to me and viceversa
         		FriendOrAlike myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         		createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myOtherFriendAlsoMutual);
         	}
         	
         	int totalInteractionsAsWeight = myFriend.getToOtherUserInteractions(myOtherFriendAlsoMutualWithMeId).getTotalInteractions();
         	
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode, totalInteractionsAsWeight);         	
         	myFriendsToMutualFriendsTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);         	
			}
      }
	}
	
	
	private void createFriendsOfFriends() {
		Map<String, FriendOrAlike> otherUsersMap = world.getOtherUsersMap();
		Iterator<Entry<String, FriendOrAlike>> friendsOfMyFriendsIterator = otherUsersMap.entrySet().iterator();
		while (friendsOfMyFriendsIterator.hasNext()) {
			Entry<String, FriendOrAlike> friendOfMyFriendEntry = friendsOfMyFriendsIterator.next();
			String friendOfMyFriendId = friendOfMyFriendEntry.getKey();

			FriendOrAlike friendOfMyFriend = friendOfMyFriendEntry.getValue();
			Set<String> friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds = friendOfMyFriend.getMutualFriends();

			Node friendOfMyFriendNode = createNode(friendOfMyFriend);
			friendOfFriendNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

			for (String friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds ) {
				Node myFriendNode = getOrCreateFriendNode(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				
				int myfriendToHimFriendInteractionsAsWeight = myFriendsMap.get(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId).getToOtherUserInteractionsCount(friendOfMyFriendId);
		      Edge myFriendWithHimFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode, myfriendToHimFriendInteractionsAsWeight);
		      friendsOfFriendsWithFriendsTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, myFriendWithHimFriendEdge);
		      
		      int friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions = friendOfMyFriend.getToOtherUserInteractionsCount(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				Edge friendOfMyFriendToHimFrienThatIsMutualFriendEdge = createEdge(friendOfMyFriendNode,myFriendNode,friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions);
				friendsOfFriendsWithFriendsTable.put(friendOfMyFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendToHimFrienThatIsMutualFriendEdge);
			}
		}
	}
	
	private Node createNode(User user) {
		String id = user.getId();
		Node node = graphFactory.newNode(id);
		NodeData nodeData = node.getNodeData();
		nodeData.setLabel(id);
		nodeData.setSize( user.getOwnPostsCount() );
		int appreciation = user.getOwnLikedPostsCount() + user.getOwnPostsResharingCount();
		float normalizedAppreciation = (appreciation-appreciationsMin)/appreciationsDenominator;
		nodeData.setAlpha( normalizedAppreciation );
		
		directedGraph.addNode(node);
		return node;
	}
	
	
	private Edge createEdge(Node firstNode, Node secondNode, float weight) {
		Edge createdEdge = graphFactory.newEdge(firstNode, secondNode, weight, true);
   	directedGraph.addEdge(createdEdge);
   	return createdEdge;
	}
	
	
	private Node createAndMaintainOrRetrieveFriendNode(User myFriend) {
		String myFriendId = myFriend.getId();
		Node myFriendNode = null;
		if (myFriendsNodesMap.containsKey(myFriendId)) {
			myFriendNode = myFriendsNodesMap.get(myFriendId);
			if (directedGraph.contains(myFriendNode)) {
				directedGraph.addNode(myFriendNode);
			}
		} else {
			myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}

	private Node getOrCreateFriendNode(String myFriendId) {
		Node myFriendNode = null;
		if (myFriendsNodesMap.containsKey(myFriendId)) {
			myFriendNode = myFriendsNodesMap.get(myFriendId);
			if (directedGraph.contains(myFriendNode)) {
				directedGraph.addNode(myFriendNode);
			}
		} else {
			FriendOrAlike myFriend = myFriendsMap.get(myFriendId);
			
			myFriendNode = createNode(myFriend);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}
	
	
	/*private void createAndMaintainOrRetrieveFriendEdgesWithMe(Node myFriendNode, Node egoNode, float myWeight, float myfriendWeight) {
		Edge myFriendWithMeEdge = createEdge(egoNode, myFriendNode, myWeight);
      myFriendsWithMeEdgesMap.put(myFriendNode.getNodeData().getId(), myFriendWithMeEdge);
	}*/

	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(User myFriend) {
		String myFriendId = myFriend.getId();
		if (myFriendsNodesMap.containsKey(myFriendId))
			return myFriendsNodesMap.get(myFriendId);
		else {			
			Node myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
	      
	      int meToFriendInteractionsAsWeight = ego.getToOtherUserInteractionsCount(myFriendId);
	      Edge meWithMyfriendEdge = createEdge(egoNode, myFriendNode, meToFriendInteractionsAsWeight);
	      
	      int myfriendToMeInteractionsAsWeight = myFriend.getToOtherUserInteractionsCount(myFriendId);
	      Edge myFriendWithMeEdge = createEdge(myFriendNode, egoNode, myfriendToMeInteractionsAsWeight);
	      
	      myFriendsWithMeEdgesAndViceversaTable.put(ego.getId(), myFriendId, meWithMyfriendEdge);
	      myFriendsWithMeEdgesAndViceversaTable.put(myFriendId, ego.getId(), myFriendWithMeEdge);
	      
	      return myFriendNode;
		}		
	}
	
	
	private boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( myFriendsToMutualFriendsTable.contains(firstUserId, secondUserId) && myFriendsToMutualFriendsTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}

	public void testGraph() {

		graphNodesCount = directedGraph.getNodeCount();
		graphEdgesCount = directedGraph.getEdgeCount();
		
		System.out.println( "\tmy friends:\n" 
				+"\t\tnodes (map): "+ myFriendsMap.size()+" = "+myFriendsNodesMap.size()+"\n"
				+"\t\tedges to me (map): "+myFriendsWithMeEdgesAndViceversaTable.size()+"\n"
				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
				+"\tfriends of my friends (maps):\n"
				+"\t\tnodes (map): "+friendOfFriendNodeMap.size()+" = "+world.getOtherUsersMap().size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
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
		directedGraph.clear();
		egoNode = null;
		myFriendsNodesMap.clear();
		myFriendsWithMeEdgesAndViceversaTable.clear();
		myFriendsToMutualFriendsTable.clear();
		graphNodesCount=0;
		graphEdgesCount=0;
	}
	
	
}
