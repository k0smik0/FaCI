package net.iubris.facri.graph.generator.graphstream;


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

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GraphstreamInteractionsGraphGenerator implements InteractionsGraphGenerator {
	
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
	private final Table<String, String, Edge> myFriendsToMutualFriendsTable = HashBasedTable.create();
	
	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsOfFriendsWithFriendsEdgesTable = HashBasedTable.create();
	
	private final World world;
	private final Map<String, FriendOrAlike> myFriendsMap;
	
	private final Graph graph;

	private Ego ego;
	private Node egoNode;
	
	private int appreciationsMin;
	private int appreciationsDenominator;
	private int postsCountMin;
	private int postsCountDenominator;
	
	private int graphNodesCount;
	private int graphEdgesCount;
	private Integer interactionsMin;
	private int interactionsDenominator;
	

	@Inject
	public GraphstreamInteractionsGraphGenerator(World world) {
		this.graph = new MultiGraph("Interactions",false,true);
		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
//		MultiGraph multiGraph = new MultiGraph("a");
		this.world = world;
		this.myFriendsMap = world.getMyFriendsMap();
	}	

	private void retrieveDataForNormalizations() {
		Set<Integer> postsCountRange = world.getPostsCountRange();
		postsCountMin = Collections.min(postsCountRange);
		postsCountDenominator = Collections.max(postsCountRange)-postsCountMin;
		System.out.print("["+postsCountMin+", "+postsCountDenominator+"] ");
		
		Set<Integer> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = Collections.min(appreciationsRange);
		appreciationsDenominator = Collections.max(appreciationsRange)-appreciationsMin;
		System.out.print("["+appreciationsMin+", "+appreciationsDenominator+"] ");
		
		Set<Integer> interactionsRange = world.getInteractionsRange();
		interactionsMin = Collections.min(interactionsRange);
		interactionsDenominator = Collections.max(interactionsRange)-interactionsMin;
		System.out.print("["+interactionsMin+", "+interactionsDenominator+"] ");
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	@Override
	public void generateAll() {
		retrieveDataForNormalizations();
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
		createFriendsOfFriends();
		egoNode.addAttribute("stroke-color", "red");
	}
	
	@Override
	public void generateMyFriends() {
		retrieveDataForNormalizations();
		createMyFriendsOnly();
	}
	
	@Override
	public void generateMeWithMyFriends() {
		retrieveDataForNormalizations();
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
//		egoNode.addAttribute("stroke-color", "red");
		egoNode.addAttribute("ui.class", "ego");
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		retrieveDataForNormalizations();
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
      	if (includeMyUserNode) {
      		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myFriend);
//      		System.out.println(egoNode);
      	} else {
      		myFriendNode = getOrCreateFriendNode(myFriendId);
//      		System.out.println(myFriendNode);
      	}
//      	myFriendsNodesMap.put(myFriendId, myFriendNode);
	         
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();        
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         	
//         	// that is: a cell in myFriendsToMutualFriendsTable contains edge between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
         	
         	// this creation below could be decrease outer while iterations ?
         	Node myOtherFriendAlsoMutualNode = getOrCreateFriendNode( /*myFriendsMap.get(myOtherFriendAlsoMutualWithMeId)*/ myOtherFriendAlsoMutualWithMeId);
         	if (includeMyUserNode) {
         		
         		// my mutual friend to me and viceversa
         		FriendOrAlike myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         		createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myOtherFriendAlsoMutual);
         	}
         	
         	int totalInteractionsAsWeight = myFriend.getToOtherUserInteractions(myOtherFriendAlsoMutualWithMeId).getTotalInteractions();
         	float normalizedInteractions = ((float)totalInteractionsAsWeight-interactionsMin)/interactionsDenominator;
         	
//         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode, totalInteractionsAsWeight);
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode, normalizedInteractions);
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
			friendOfMyFriendNode.addAttribute("stroke-color", "black");
			friendOfFriendNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

			for (String friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds ) {
				Node myFriendNode = getOrCreateFriendNode(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				
				int myfriendToHimFriendInteractionsAsWeight = myFriendsMap.get(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId).getToOtherUserInteractionsCount(friendOfMyFriendId);
		      Edge myFriendWithHimFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode, myfriendToHimFriendInteractionsAsWeight);
		      friendsOfFriendsWithFriendsEdgesTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, myFriendWithHimFriendEdge);
		      
		      int friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions = friendOfMyFriend.getToOtherUserInteractionsCount(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				Edge friendOfMyFriendToHimFrienThatIsMutualFriendEdge = createEdge(friendOfMyFriendNode,myFriendNode,friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions);
				friendsOfFriendsWithFriendsEdgesTable.put(friendOfMyFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendToHimFrienThatIsMutualFriendEdge);
			}
		}
	}
	
	private Node createNode(User user) {
		String id = user.getId();
		Node node = graph.addNode(id);
		
		float normalizedSize = ((float)user.getOwnPostsCount()-postsCountMin)/postsCountDenominator;
//		System.out.print("("+user.getOwnPostsCount()+"-"+postsCountMin+")/"+postsCountDenominator+" = ");
		node.setAttribute("ui.size", normalizedSize+"gu");
//		System.out.print(normalizedSize+" ");
		
		int appreciation = user.getOwnLikedPostsCount() + user.getOwnPostsResharingCount();
		float normalizedAppreciation = ((float)appreciation-appreciationsMin)/appreciationsDenominator;
		node.setAttribute("ui.color", normalizedAppreciation);
//		System.out.println(normalizedAppreciation+" ");

		return node;
	}
	
	
	private Edge createEdge(Node firstNode, Node secondNode, float weight) {
		String edgeId = firstNode.getId()+"_to_"+secondNode.getId();
//		Edge createdEdge = graph.addEdge(edgeId, firstNode, secondNode, true);
		Edge createdEdge = graph.addEdge(edgeId, firstNode.getId(), secondNode.getId(), true);
//		createdEdge.setAttribute("ui.arrow-size", weight);
		createdEdge.setAttribute("ui.color", weight*100);
//		System.out.print(createdEdge.getId()+" ");
   	return createdEdge;
	}
	
	
	/*private Node getOrCreateFriendNode(User myFriend) {
		String myFriendId = myFriend.getId();
		Node myFriendNode = null;
//		if (myFriendsNodesMap.containsKey(myFriendId)) {
//			myFriendNode = myFriendsNodesMap.get(myFriendId);
////			graph.addNode(myFriendNode.getId());
//		} else {
//			myFriendNode = createNode(myFriend);
//	      myFriendsNodesMap.put(myFriendId, myFriendNode);
//		}
		myFriendNode = graph.getNode(myFriendId);
		if (myFriendNode==null)
			myFriendNode = createNode(myFriendId);
		return myFriendNode;
	}*/

	private Node getOrCreateFriendNode(String myFriendId) {
		FriendOrAlike myFriend = myFriendsMap.get(myFriendId);
		Node myFriendNode = graph.getNode(myFriendId); 
		if (myFriendNode==null) {
			myFriendNode = createNode(myFriend);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}		
		myFriendNode.addAttribute("stroke-color", "yellow");
		/*if (myFriendsNodesMap.containsKey(myFriendId)) {
			myFriendNode = myFriendsNodesMap.get(myFriendId);
		} else {
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}*/
		
		return myFriendNode;
	}
	
	
	/*private void createAndMaintainOrRetrieveFriendEdgesWithMe(Node myFriendNode, Node egoNode, float myWeight, float myfriendWeight) {
		Edge myFriendWithMeEdge = createEdge(egoNode, myFriendNode, myWeight);
      myFriendsWithMeEdgesMap.put(myFriendNode.getNodeData().getId(), myFriendWithMeEdge);
	}*/

	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(User myFriend) {
		String myFriendId = myFriend.getId();
		Node myFriendNode = graph.getNode(myFriendId);
		if (myFriendNode!=null)
			return myFriendNode;
		else {			
			myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
	      
	      int meToFriendInteractionsAsWeight = ego.getToOtherUserInteractionsCount(myFriendId);
	      float myNormalizedInteractions = ((float)meToFriendInteractionsAsWeight-interactionsMin)/interactionsDenominator;
//	      Edge meWithMyfriendEdge = createEdge(egoNode, myFriendNode, meToFriendInteractionsAsWeight);
	      Edge meWithMyfriendEdge = createEdge(egoNode, myFriendNode, myNormalizedInteractions);
	      
	      int myfriendToMeInteractionsAsWeight = myFriend.getToOtherUserInteractionsCount(myFriendId);
	      float myFriendToMeNormalizedInteractions = ((float)myfriendToMeInteractionsAsWeight-interactionsMin)/interactionsDenominator;
//	      Edge myFriendWithMeEdge = createEdge(myFriendNode, egoNode, myfriendToMeInteractionsAsWeight);
	      Edge myFriendWithMeEdge = createEdge(myFriendNode, egoNode, myFriendToMeNormalizedInteractions);
	      
//	      System.out.print("{"+myNormalizedInteractions+" vs "+myFriendToMeNormalizedInteractions+"} ");
	      
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

		graphNodesCount = graph.getNodeCount();
		graphEdgesCount = graph.getEdgeCount();
		
		 Map<String, FriendOrAlike> friendOfFriendMap = world.getOtherUsersMap();
		
		System.out.println( "\tmy friends:\n" 
				+"\t\tnodes (map): "+ myFriendsMap.size()+" = "+myFriendsNodesMap.size()+"\n"
				+"\t\tedges to with (map): "+myFriendsWithMeEdgesAndViceversaTable.size()+"\n"
				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
				+"\tfriends of my friends (maps):\n"
				+"\t\tnodes (map): "+friendOfFriendMap.size()+" = "+friendOfFriendNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
				+"\t\tedges to their/my friends (map): "+friendsOfFriendsWithFriendsEdgesTable.size()+"\n"
				+"\n"
				+"\tgraph nodes:"+graphNodesCount+"\n"
				+"\tgraph edges:"+graphEdgesCount+"\n"
		);
//		for (Edge edge: myFriendsWithMeEdgesAndViceversaTable.values())
//			System.out.print(edge.getId()+" ");
	}
	

	
//	private void incrementMyFriendsToMutualFriendsEdges(int edges) {
//		myFriendsToMutualFriendsEdges+=edges;
//	}
	
	public void clear() {
		graph.clear();
		egoNode = null;
		myFriendsNodesMap.clear();
		myFriendsWithMeEdgesAndViceversaTable.clear();
		myFriendsToMutualFriendsTable.clear();
		graphNodesCount=0;
		graphEdgesCount=0;
	}
}
