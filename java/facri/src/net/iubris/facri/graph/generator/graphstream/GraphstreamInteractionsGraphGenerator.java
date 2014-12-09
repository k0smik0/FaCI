package net.iubris.facri.graph.generator.graphstream;


import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.facri.graph.generator.InteractionsGraphGenerator;
import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.utils.Memory;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.Viewer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@Singleton
public class GraphstreamInteractionsGraphGenerator implements InteractionsGraphGenerator, GraphstreamGraphGenerator {
	
	private final World world;
	
	private final Map<String, FriendOrAlike> myFriendsFromWorldMap;
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
	private final Table<String, String, Edge> myFriendsToMutualFriendsTable = HashBasedTable.create();

	private final Map<String, FriendOrAlike> friendOfFriendFromWorldMap;
	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsOfFriendsWithFriendsEdgesTable = HashBasedTable.create();
	
	private final Graph graph;

	
	private Ego ego;
	private Node egoNode;
	
	private int appreciationsMin;
	private int appreciationsDenominator;
	private int postsCountMin;
	private int postsCountDenominator;
	
	private int graphNodesCount;
	private int graphEdgesCount;
	private int interactionsMin;
	private int interactionsDenominator;
	
	private int graphSpeed = 3; //ms
	
	private final String egoNodeUiClass = "ego";
	private final String friendNodeUiClass = "friend, all";
	private final String friendOfFriendNodeUiClass = "friendof";
	
	private final String meToMyfriendEdgeUiClass = "metomyfriend";
	private final String myfriendToMeEdgeUiClass = "myfriendtome";
	private final String myfriendWithMutualFriendEdgeUiClass = "myfriendwithmutualfriend";
	private final String friendofmyfriendWithmyfriendEdgeUiClass = "friendofmyfriendwithmyfriend";
	

	@Inject
	public GraphstreamInteractionsGraphGenerator(World world) {
		this.graph = new MultiGraph("Interactions",false,true);
		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
//		MultiGraph multiGraph = new MultiGraph("a");
		this.world = world;
		this.myFriendsFromWorldMap = world.getMyFriendsMap();
		this.friendOfFriendFromWorldMap = world.getOtherUsersMap();
	}	

	private void retrieveDataForNormalizations() {
		Set<Integer> postsCountRange = world.getPostsCountRange();
		postsCountMin = Collections.min(postsCountRange);
		postsCountDenominator = Collections.max(postsCountRange)-postsCountMin;
//System.out.print("["+postsCountMin+", "+postsCountDenominator+"] ");
		
		Set<Integer> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = Collections.min(appreciationsRange);
		appreciationsDenominator = Collections.max(appreciationsRange)-appreciationsMin;
//System.out.print("["+appreciationsMin+", "+appreciationsDenominator+"] ");
		
		Set<Integer> interactionsRange = world.getInteractionsRange();
		interactionsMin = Collections.min(interactionsRange);
		interactionsDenominator = Collections.max(interactionsRange)-interactionsMin;
//System.out.print("["+interactionsMin+", "+interactionsDenominator+"] ");
	}
	
	public void prepareForDisplay() {
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		graph.display();
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		viewer.enableAutoLayout();
	}
	
	@Override
	public Graph getGraph() {
		return graph;
	}
	
	public Node getEgoNode() {
		return egoNode;
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
		testGraph();
//		garbageUselessFriendsOfFriends();
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		retrieveDataForNormalizations();
		createMyFriendsOnly();
		createFriendsOfFriends();
	}
	
	@Override
	public void generateMeWithMyFriendsAndTheirFriends() {
		retrieveDataForNormalizations();
		this.egoNode = createMe();
		createMyFriendsWithMe(egoNode);
		createFriendsOfFriends();
	}

	
	private Node createMe() {
		this.ego = world.getMyUser();
      this.egoNode = createNode(ego);
      this.egoNode.addAttribute("ui.class", egoNodeUiClass);
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
	private void createMyFriendsWithMe(Node egoNode) {
		createMyFriends(true);
	}
	
	private void createMyFriends(boolean includeMyuserNode) {
		Map<String, FriendOrAlike> myFriendsMap = world.getMyFriendsMap();
		Iterator<Entry<String, FriendOrAlike>> myFriendsMapIterator = myFriendsMap.entrySet().iterator();
		while (myFriendsMapIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendEntry = myFriendsMapIterator.next();
//      	myFriendsMap.entrySet().parallelStream().forEach( new Consumer<Entry<String, FriendOrAlike>>() {
//   			@Override
//   			public void accept(Entry<String, FriendOrAlike> myFriendEntry) {
   			
      	String myFriendId = myFriendEntry.getKey();
      	FriendOrAlike myFriend = myFriendEntry.getValue();

      	// eventually, creates edges with me 
      	final Node myFriendNode = getOrCreateFriendNode(myFriend, includeMyuserNode);
      	myFriendNode.addAttribute("ui.class", friendNodeUiClass);
//      	myFriendNode.addAttribute("stroke-width","10px");
	         
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
//         mutualFriendsIds.stream().forEach( new Consumer<String>() {
//				@Override
//				public void accept(String myOtherFriendAlsoMutualWithMeId) {
         	
//         	// that is: a cell in myFriendsToMutualFriendsTable contains edges between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
//         		return;
         	}
         	
         	// this creation below could be decrease outer while iterations ?
         	Node myOtherFriendAlsoMutualNode = getOrCreateFriendNode(myOtherFriendAlsoMutualWithMeId);
         	if (includeMyuserNode) {
         		// my mutual friend to me and viceversa
         		FriendOrAlike myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         		createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myOtherFriendAlsoMutual);
         	}
         	myOtherFriendAlsoMutualNode.addAttribute("ui.class", friendNodeUiClass);
         	
         	int myFriendToMytualFriendInteractions = myFriend.getToOtherUserInteractionsCount(myOtherFriendAlsoMutualWithMeId);
         	float myFriendToMytualFriendNormalizedInteractions = ((float)myFriendToMytualFriendInteractions-interactionsMin)/interactionsDenominator;
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode, myFriendToMytualFriendNormalizedInteractions, myfriendWithMutualFriendEdgeUiClass);
         	myFriendsToMutualFriendsTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);
         	
         	int myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsCount = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId).getToOtherUserInteractionsCount(myFriendId);
         	float myOtherFriendAlsoMutualWithMeToMyFriendSubjectNormalizedInteractionsCount = ((float)myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsCount-interactionsMin)/interactionsDenominator;
         	Edge myOtherFriendAlsoMutualWithMeToMyFriendSubjectEdge = createEdge(myOtherFriendAlsoMutualNode, myFriendNode, myOtherFriendAlsoMutualWithMeToMyFriendSubjectNormalizedInteractionsCount, myfriendWithMutualFriendEdgeUiClass);
         	myFriendsToMutualFriendsTable.put(myOtherFriendAlsoMutualWithMeId, myFriendId, myOtherFriendAlsoMutualWithMeToMyFriendSubjectEdge);
         	
         	
//				// one edge, with weight as difference
//		      float diff = myfriendToHimFriendInteractionsAsWeight - friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions;
//		      Edge edge = graph.addEdge(myFriendNode.getId()+"_to_"+egoNode.getId(), myFriendNode, egoNode, true);
//		      edge.setAttribute("ui.size", 1-diff);
//		      edge.setAttribute("ui.color", diff);
//		      edge.setAttribute("ui.class", friendofmyfriendWithmyfriendEdgeUiClass);
//		      myFriendsWithMeEdgesAndViceversaTable.put(friendOfMyFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, edge);
//		      myFriendsWithMeEdgesAndViceversaTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, edge);
			}
//		});
      }
//		});
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
			friendOfMyFriendNode.addAttribute("ui.class", friendOfFriendNodeUiClass );
			friendOfFriendNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

			for (String friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds ) {
				Node myFriendNode = getOrCreateFriendNode(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				myFriendNode.addAttribute("ui.class", friendNodeUiClass);
				
				int myfriendToHimFriendInteractionsAsWeight = myFriendsFromWorldMap.get(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId).getToOtherUserInteractionsCount(friendOfMyFriendId);
		      Edge myFriendWithHimFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode, myfriendToHimFriendInteractionsAsWeight, friendofmyfriendWithmyfriendEdgeUiClass);
		      friendsOfFriendsWithFriendsEdgesTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, myFriendWithHimFriendEdge);
		      
		      int friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions = friendOfMyFriend.getToOtherUserInteractionsCount(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
				Edge friendOfMyFriendToHimFrienThatIsMutualFriendEdge = createEdge(friendOfMyFriendNode,myFriendNode,friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions, friendofmyfriendWithmyfriendEdgeUiClass);
				friendsOfFriendsWithFriendsEdgesTable.put(friendOfMyFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendToHimFrienThatIsMutualFriendEdge);
			}
		}
	}
	
	private Node getOrCreateFriendNode(User myFriend, boolean includeMyuserNode) {
		Node myFriendNode = null;
		if (includeMyuserNode) {
   		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myFriend);
   	} else {
   		myFriendNode = getOrCreateFriendNode(myFriend.getId());
   	}
		return myFriendNode;
	}
	
	private Node getOrCreateFriendNode(String myFriendId) {
		FriendOrAlike myFriend = myFriendsFromWorldMap.get(myFriendId);
		Node myFriendNode = graph.getNode(myFriendId); 
		if (myFriendNode==null) {
			myFriendNode = createNode(myFriend);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}
	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(User myFriend) {
		String myFriendId = myFriend.getId();
		Node myFriendNode = graph.getNode(myFriendId);
		if (myFriendNode!=null)
			return myFriendNode;
		else {			
			myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
	      
	      int meToFriendInteractionsAsWeight = ego.getToOtherUserInteractionsCount(myFriendId);
	      float mineToMyFriendNormalizedInteractions = ((float)meToFriendInteractionsAsWeight-interactionsMin)/interactionsDenominator;
	      Edge meToMyfriendEdge = createEdge(egoNode, myFriendNode, mineToMyFriendNormalizedInteractions, meToMyfriendEdgeUiClass);
	      
	      int myfriendToMeInteractionsAsWeight = myFriend.getToOtherUserInteractionsCount(myFriendId);
	      float myFriendToMeNormalizedInteractions = ((float)myfriendToMeInteractionsAsWeight-interactionsMin)/interactionsDenominator;
	      Edge myFriendToMeEdge = createEdge(myFriendNode, egoNode, myFriendToMeNormalizedInteractions, myfriendToMeEdgeUiClass);
//	      graph.removeEdge(meToMyfriendEdge);
//	      graph.removeEdge(myFriendToMeEdge);
	      
	      String egoId = ego.getId();
	      myFriendsWithMeEdgesAndViceversaTable.put(egoId, myFriendId, meToMyfriendEdge);
	      myFriendsWithMeEdgesAndViceversaTable.put(myFriendId, egoId, myFriendToMeEdge);
	      
	      
//	      // one edge, with weight as difference
//	      float diff = mineToMyFriendNormalizedInteractions-myFriendToMeNormalizedInteractions;
//	      Edge edge = graph.addEdge(myFriendNode.getId()+"_to_"+egoNode.getId(), myFriendNode, egoNode, true);
//	      edge.setAttribute("ui.size", 1-diff);
//	      edge.setAttribute("ui.color", diff);
//	      edge.setAttribute("ui.class", myfriendWithMeEdgeUiClass);
//	      myFriendsWithMeEdgesAndViceversaTable.put(ego.getId(), myFriendId, edge);
//	      myFriendsWithMeEdgesAndViceversaTable.put(myFriendId, ego.getId(), edge);
	      
	      return myFriendNode;
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
		
		pause(graphSpeed );

		return node;
	}
	
	private Edge createEdge(Node firstNode, Node secondNode, float normalizedWeight, String uiClass) {
		String edgeId = firstNode.getId()+"_to_"+secondNode.getId();
//		Edge createdEdge = graph.addEdge(edgeId, firstNode, secondNode, true);
		Edge createdEdge = graph.addEdge(edgeId, firstNode.getId(), secondNode.getId(), true);
		
		createdEdge.setAttribute("ui.class", uiClass);
//		createdEdge.setAttribute("ui.arrow-size", weight);
//		float color = Math.abs(weight*1);
		createdEdge.setAttribute("ui.color", normalizedWeight);
//		float size = Math.abs(weight);
//		System.out.print(size+" ");
		// the higher weight, the longer and larger, so we complement
		createdEdge.setAttribute("ui.size", 2*(1-normalizedWeight));
//		createdEdge.setAttribute("ui.arrow-size", normalizedWeight);
//		System.out.print(createdEdge.getId()+" ");
		
		pause(graphSpeed);
		
   	return createdEdge;
	}
	
	private void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
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
		
		System.out.println( "\tmy friends:\n" 
				+"\t\tnodes (map): "+ myFriendsFromWorldMap.size()+" = "+myFriendsNodesMap.size()+"\n"
				+"\t\tedges with me (map): "+myFriendsWithMeEdgesAndViceversaTable.size()+"\n"
				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
				+"\tfriends of my friends (maps):\n"
				+"\t\tnodes (map): "+friendOfFriendFromWorldMap.size()+" = "+friendOfFriendNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
				+"\t\tedges to their/my friends (map): "+friendsOfFriendsWithFriendsEdgesTable.size()+"\n"
				+"\n"
				+"\tgraph nodes:"+graphNodesCount+"\n"
				+"\tgraph edges:"+graphEdgesCount+"\n"
		);
//		for (Edge edge: myFriendsWithMeEdgesAndViceversaTable.values())
//			System.out.print(edge.getId()+" ");
		
//		garbageUselessFriendsOfFriends();
//		garbageUselessFriends();
	}
	
	public void clear() {
//		garbageUseless();		
//		graph.clear();
//		egoNode = null;
	}
	
	private void garbageUselessFriendsOfFriends() {
		long checkMemoryPre = Memory.checkMemory();
//		friendOfFriendFromWorldMap.clear();
		friendOfFriendNodeMap.clear();
		friendsOfFriendsWithFriendsEdgesTable.clear();
		long checkMemoryAfter = Memory.checkMemory();
		System.out.println("freed: "+(checkMemoryPre-checkMemoryAfter)+"Mb");
	}
	
	private void garbageUselessFriends() {
		long checkMemoryPre = Memory.checkMemory();
//		myFriendsFromWorldMap.clear();
		myFriendsNodesMap.clear();
		myFriendsToMutualFriendsTable.clear();
		myFriendsWithMeEdgesAndViceversaTable.clear();
		long checkMemoryAfter = Memory.checkMemory();
		System.out.println("freed: "+(checkMemoryPre-checkMemoryAfter)+"Mb");
	}
	
	private void garbageUseless() {
		long checkMemoryPre = Memory.checkMemory();
		
		garbageUselessFriends();
		garbageUselessFriendsOfFriends();
		graphNodesCount=0;
		graphEdgesCount=0;
		
		long checkMemoryAfter = Memory.checkMemory();
		System.out.println("freed: "+(checkMemoryAfter-checkMemoryPre)+"Mb");
	}
}
