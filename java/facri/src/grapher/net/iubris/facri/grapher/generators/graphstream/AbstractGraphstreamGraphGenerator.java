package net.iubris.facri.grapher.generators.graphstream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.model.world.World;
import net.iubris.facri.utils.MemoryUtil;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


public abstract class AbstractGraphstreamGraphGenerator implements GraphstreamGraphGenerator {

	protected final GraphsHolder graphsHolder;
	
	protected final Graph graph;
	protected final World world;
	
	protected final Map<String, FriendOrAlike> myFriendsFromWorldMap;
	protected final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	protected final Table<String, String, Boolean> myFriendsWithMeEdgesAndViceversaComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
	protected final Table<String, String, Boolean> myFriendsToMutualFriendsEdgesComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> myFriendsToMutualFriendsEdgesTable = HashBasedTable.create();
	
	protected final Map<String, FriendOrAlike> friendOfFriendFromWorldMap;
	protected final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	protected final Table<String, String, Boolean> friendsOfFriendsWithFriendsEdgesComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> friendsOfFriendsWithFriendsEdgesTable = HashBasedTable.create();
	
	protected final String egoNodeUiClass = "ego";
	protected final String friendNodeUiClass = "friend, all";
	protected final String friendOfFriendNodeUiClass = "friendof";
	protected final String meToMyfriendEdgeUiClass = "metomyfriend";
	protected final String myfriendToMeEdgeUiClass = "myfriendtome";
	protected final String myfriendWithMutualFriendEdgeUiClass = "myfriendwithmutualfriend";
	protected final String friendofmyfriendWithmyfriendEdgeUiClass = "friendofmyfriendwithmyfriend";
	
	protected Ego ego;
	protected Node egoNode;
	protected int graphSpeed = 3; //ms
	
	private int graphNodesCountTest;
	private int graphEdgesCountTest;
	
	public AbstractGraphstreamGraphGenerator(GraphsHolder graphsHolder, Graph graph, World world) {
		this.graphsHolder = graphsHolder;
		this.graph = graph;
		this.world = world;
		this.myFriendsFromWorldMap = world.getMyFriendsMap();
		this.friendOfFriendFromWorldMap = world.getOtherUsersMap();
	}
	
	@Override
	public void generateMyFriends() {
		createMyFriendsOnly();
	}
	protected void createMyFriendsOnly() {
		createMyFriends(false);
	}
	
	@Override
	public void generateMeWithMyFriends() {
		this.egoNode = createMe();
		createMyFriendsWithMe();
//		testGraph();
	}
	@Override
	public void generateMeWithMyFriendsAndTheirFriends() {
		this.egoNode = createMe();
		createMyFriendsWithMe();
		createFriendsOfFriendsWithMyFriends();
	}
	/**
	 * create my friends graph including my node and relative edges
	 * @param egoNode
	 */
	protected void createMyFriendsWithMe() {
		createMyFriends(true);
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		createMyFriendsOnly();
		createFriendsOfFriendsWithMyFriends();
	}
	
	protected abstract void createMyFriends(boolean includeMyuserNode);
	protected abstract void createFriendsOfFriendsWithMyFriends();
	
	protected Node createMe() {
		this.ego = world.getMyUser();
      this.egoNode = createNode(ego);
      this.egoNode.addAttribute("ui.class", egoNodeUiClass);
      return egoNode;
	}
	protected Node getEgoNode() {
		return egoNode;
	}

	protected void pause() {
		try {
			Thread.sleep(graphSpeed);
		} catch (InterruptedException e) {}
	}
	
	protected Node getOrCreateFriendNodeAndEventuallyEdgesWithMe(User myFriend, boolean includeMyuserNode) {
		Node myFriendNode = null;
		if (includeMyuserNode) {
   		myFriendNode = getOrCreateFriendNodeAndEdgesWithMe(myFriend);
   	} else {
   		myFriendNode = getOrCreateFriendNode(myFriend.getUid());
   	}
		myFriendNode.addAttribute("ui.class", friendNodeUiClass);
		return myFriendNode;
	}
	
	protected Node getOrCreateFriendNode(String myFriendId) {
		FriendOrAlike myFriend = myFriendsFromWorldMap.get(myFriendId);
		Node myFriendNode = graph.getNode(myFriendId); 
		if (myFriendNode==null) {
			myFriendNode = createNode(myFriend);
			myFriendNode.addAttribute("ui.class", friendNodeUiClass);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}
	
	protected abstract Node createNode(User user);
	
	protected abstract Node getOrCreateFriendNodeAndEdgesWithMe(User myFriend);
	
	protected Edge createEdge(Node firstNode, Node secondNode, boolean directed, float weight, String uiClass) {
		String edgeId = buildEdgeId(firstNode, secondNode);
		Edge createdEdge = graph.addEdge(edgeId, firstNode.getId(), secondNode.getId(), directed);
			
		createdEdge.setAttribute("ui.class", uiClass);
		
		return createdEdge;
	}
	protected String buildEdgeId(Node firstNode, Node secondNode) {
		String edgeId = firstNode.getId()+"_to_"+secondNode.getId();
		return edgeId;
	}
	
	protected abstract boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId);
	
	@Override
	public Graph getGraph() {
		return graph;
	}
	
	public void testGraph() {

		graphNodesCountTest = graph.getNodeCount();
		graphEdgesCountTest = graph.getEdgeCount();

		System.out.println("Ego:\n"
				+"degree: "+egoNode.getDegree()+"\n"
				+"in-degree: "+egoNode.getInDegree()+"\n"
				+"out-degree: "+egoNode.getOutDegree()+"\n\n"
				);
		
		System.out.println(
				"\tme: "+egoNode.getInDegree()+","+egoNode.getOutDegree()+"\n"
				+"\tmy friends:\n" 
				+"\t\tnodes (map): "+ myFriendsFromWorldMap.size()+" = "+myFriendsNodesMap.size()+"\n"
				+"\t\tedges with me (map): "+myFriendsWithMeEdgesAndViceversaTable.size()+"(2*"+myFriendsWithMeEdgesAndViceversaTable.row(egoNode.getId()).values().size()+")\n"
				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsEdgesTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
				+"\tfriends of my friends (maps):\n"
				+"\t\tnodes (map): "+friendOfFriendFromWorldMap.size()+" = "+friendOfFriendNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
				+"\t\tedges to their/my friends (map): "+friendsOfFriendsWithFriendsEdgesTable.size()+"\n"
				+"\n"
				+"\tgraph nodes:"+graphNodesCountTest+"\n"
				+"\tgraph edges:"+graphEdgesCountTest+"\n"
		);
//		for (Edge edge: myFriendsWithMeEdgesAndViceversaTable.values())
//			System.out.print(edge.getId()+" ");
		
//		garbageUselessFriendsOfFriends();
//		garbageUselessFriends();
		
//		reparseGraphCSS();
		garbageUseless();
	}
	
	@Override
	public void clear() {
//		garbageUseless();
//		graph.clear();
//		egoNode = null;
	}
	
	protected void garbageUselessFriendsOfFriends() {
//		long checkMemoryPre = Memory.checkMemory();
		friendOfFriendNodeMap.clear();
		
		friendsOfFriendsWithFriendsEdgesComputedTable.clear();
		friendsOfFriendsWithFriendsEdgesTable.clear();
//		long checkMemoryAfter = 
				MemoryUtil.runGarbageCollector();
//		System.out.println("released: "+(checkMemoryPre-checkMemoryAfter)+"Mb");
	}
	
	protected void garbageUselessFriends() {
//		long checkMemoryPre = Memory.checkMemory();
		myFriendsNodesMap.clear();
		
		myFriendsToMutualFriendsEdgesComputedTable.clear();
		myFriendsToMutualFriendsEdgesTable.clear();
		
		myFriendsWithMeEdgesAndViceversaComputedTable.clear();
		myFriendsWithMeEdgesAndViceversaTable.clear();
//		long checkMemoryAfter = 
				MemoryUtil.runGarbageCollector();
//		System.out.println("released: "+(checkMemoryPre-checkMemoryAfter)+"Mb");
	}
	
	public void garbageUseless() {
		garbageUselessFriends();
		garbageUselessFriendsOfFriends();

//		long checkMemoryPre = 
				MemoryUtil.runGarbageCollector();
		graphNodesCountTest=0;
		graphEdgesCountTest=0;
//		long checkMemoryAfter = Memory.checkMemory();
//		System.out.println("released: "+(checkMemoryAfter-checkMemoryPre)+"Mb");
	}
}
