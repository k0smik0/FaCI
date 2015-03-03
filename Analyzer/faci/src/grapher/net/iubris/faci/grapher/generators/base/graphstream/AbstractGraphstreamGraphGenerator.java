/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (AbstractGraphstreamGraphGenerator.java) is part of facri.
 * 
 *     AbstractGraphstreamGraphGenerator.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     AbstractGraphstreamGraphGenerator.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.generators.base.graphstream;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.utils.MemoryUtil;
import net.iubris.faci.utils.Printer;
import net.iubris.faci.utils.Timing;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;


public abstract class AbstractGraphstreamGraphGenerator implements GraphstreamGraphGenerator {

	protected final GraphsHolder graphsHolder;
	
	protected final Graph graph;
	protected final World world;
	
	protected final Map<String, Friend> myFriendsFromWorldMap;
	protected final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	protected final Table<String, String, Boolean> myFriendsWithMeEdgesAndViceversaComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
	protected final Table<String, String, Boolean> myFriendsToMutualFriendsEdgesComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> myFriendsToMutualFriendsEdgesTable = HashBasedTable.create();
	
	protected final Map<String, FriendOfFriend> friendsOfFriendsFromWorldMap;
	protected final Map<String, Node> friendsOfFriendsNodeMap = new ConcurrentHashMap<>();
	protected final Table<String, String, Boolean> friendsOfFriendsWithFriendsEdgesComputedTable = HashBasedTable.create();
	protected final Table<String, String, Edge> friendsOfFriendsWithFriendsEdgesTable = HashBasedTable.create();
	
	protected final String egoNodeUiClass = "ego";
	protected final String friendNodeUiClass = "friend";
	protected final String friendOfFriendNodeUiClass = "friend_of_friend";
//	protected final String meToMyfriendEdgeUiClass = "metomyfriend";
//	protected final String myfriendToMeEdgeUiClass = "myfriendtome";
//	protected final String myfriendWithMutualFriendEdgeUiClass = "myfriendwithmutualfriend";
//	protected final String friendofmyfriendWithmyfriendEdgeUiClass = "friendofmyfriendwithmyfriend";
	
	protected Ego ego;
	protected Node egoNode;
	protected int graphSpeed = 3; //ms
	
	private int graphNodesCountTest;
	private int graphEdgesCountTest;

	private Boolean myFriendsWithMeCreated = new Boolean(false);
	private Boolean myFriendsOnlyCreated = new Boolean(false);
	private Boolean friendsOfFriendsWithMyFriendsCreated = new Boolean(false);
	
	public AbstractGraphstreamGraphGenerator(GraphsHolder graphsHolder, Graph graph, World world) {
		this.graphsHolder = graphsHolder;
		this.graph = graph;
		this.world = world;
		this.myFriendsFromWorldMap = world.getFriendsMap();
		this.friendsOfFriendsFromWorldMap = world.getFriendsOfFriendsMap();
	}
	
	@Override
	public void generateMeWithMyFriends() {
		if (egoNode==null)
			egoNode = createMe();
		checkAndDo(myFriendsWithMeCreated, this::createMyFriendsWithMe, "Ego+friends");
//		testGraph();
	}
	@Override
	public void generateMeWithMyFriendsAndFriendsOfFriends() {
		if (egoNode==null)
			egoNode = createMe();
		
		checkAndDo(myFriendsWithMeCreated, this::createMyFriendsWithMe, "Ego+friends");
		checkAndDo(friendsOfFriendsWithMyFriendsCreated, this::createFriendsOfFriendsWithMyFriends, "friends of friends");
		
//		garbageUselessFriends();
//		garbageUselessFriendsOfFriends();
	}
	/**
	 * create my friends graph including my node and relative edges
	 * @param egoNode
	 */
	protected int createMyFriendsWithMe() {
		int createdMyFriends = createMyFriends(true);
		myFriendsWithMeCreated = true;
		return createdMyFriends+1;
	}
	@Override
	public void generateMyFriends() {
		checkAndDo(myFriendsOnlyCreated, this::createMyFriendsOnly, "friends");
	}
	protected int createMyFriendsOnly() {
		return createMyFriends(false);
	}
	protected abstract int createMyFriends(boolean includeMyuserNode);
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		checkAndDo(myFriendsOnlyCreated, this::createMyFriendsOnly, "friends");
		checkAndDo(myFriendsOnlyCreated, this::createFriendsOfFriendsWithMyFriends, "friends of friends");
	}
	protected abstract int createFriendsOfFriendsWithMyFriends();
	
	protected Node createMe() {
		this.ego = world.getMyUser();
      this.egoNode = createNode(ego);
      this.egoNode.addAttribute("ui.class", egoNodeUiClass);
      return egoNode;
	}
	protected Node getEgoNode() {
		return egoNode;
	}
	
	@FunctionalInterface
	private interface Creator {
		int exec();
	}
	
	protected void checkAndDo(Boolean toCheck, Creator creator, String comment ) {
		if(!toCheck.booleanValue()) {
			Timing timing = new Timing();
			int returned = creator.exec();
			toCheck = new Boolean(true);
			Printer.println("Graphed "+returned+" nodes ("+comment+") in "
					+new DecimalFormat("#.##").format(timing.getTiming())+"s");
		}
	}

	protected void pause() {
		try {
			Thread.sleep(graphSpeed);
		} catch (InterruptedException e) {}
	}
	
	protected Node getOrCreateFriendNodeAndEventuallyBuildEdgesWithMe(User myFriend, boolean includeMyuserNode) {
		Node myFriendNode = null;
		if (includeMyuserNode) {
   		myFriendNode = getOrCreateFriendNodeAndEdgesWithMe(myFriend);
   	} else {
   		myFriendNode = getOrCreateFriendNode(myFriend.getUid());
   	}
		myFriendNode.addAttribute("ui.class", friendNodeUiClass);
		return myFriendNode;
	}
	protected abstract Node getOrCreateFriendNodeAndEdgesWithMe(User myFriend);
	
	protected Node getOrCreateFriendNode(String myFriendId) {
//		if (egoNode!=null)
//			getOrCreateFriendNodeAnd
		
		Friend myFriend = myFriendsFromWorldMap.get(myFriendId);
		Node myFriendNode = graph.getNode(myFriendId); 
		if (myFriendNode==null) {
			myFriendNode = createNode(myFriend);
//System.out.println("creating friend: "+myFriendNode.getId());
			myFriendNode.addAttribute("ui.class", friendNodeUiClass);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}
	
	protected abstract Node createNode(User user);
	
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
				+"\t\tnodes (map): "+friendsOfFriendsFromWorldMap.size()+" = "+friendsOfFriendsNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
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
		friendsOfFriendsNodeMap.clear();
		
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
		long releasedMemory = MemoryUtil.runGarbageCollector();
//		System.out.println("released: "+(checkMemoryPre-checkMemoryAfter)+"Mb");
		Printer.println("released: "+releasedMemory+"MB");
	}
	
	public void garbageUseless() {
		garbageUselessFriends();
		garbageUselessFriendsOfFriends();

//		long checkMemoryPre = 
			
		long releasedMemory = MemoryUtil.runGarbageCollector();
		graphNodesCountTest=0;
		graphEdgesCountTest=0;
//		long checkMemoryAfter = Memory.checkMemory();
//		System.out.println("released: "+(checkMemoryAfter-checkMemoryPre)+"Mb");
		Printer.println("released: "+releasedMemory+"MB");
	}
}
