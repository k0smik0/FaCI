/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphstreamFriendshipsGraphGenerator.java) is part of facri.
 * 
 *     GraphstreamFriendshipsGraphGenerator.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphstreamFriendshipsGraphGenerator.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.generators.specialized.friendships.graphstream;


import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.faci.grapher.generators.base.InteractionsWeigths;
import net.iubris.faci.grapher.generators.base.graphstream.AbstractGraphstreamGraphGenerator;
import net.iubris.faci.grapher.generators.specialized.friendships.FriendshipsGraphGenerator;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.model.world.users.User;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

@Singleton
public class GraphstreamFriendshipsGraphGenerator extends AbstractGraphstreamGraphGenerator implements FriendshipsGraphGenerator {
	
	private final String mewithmyfriendEdgeUiClass = "me_with_my_friend";
	private final String myfriendwithmutualfriendEdgeUiClass = "my_friend_with_mutual_friend";
	private final String friendofmyfriendwithmyfriendEdgeUiClass = "friend_of_my_friend_with_my_friend";

	@Inject
	public GraphstreamFriendshipsGraphGenerator(GraphsHolder graphsHolder, World world) {
		super(graphsHolder, graphsHolder.getFriendshipsGraph(), world);
		graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+"friendships.css')");
	}
	
	@Override
	protected int createMyFriends(boolean includeMyUserNode) {
		Map<String, Friend> myFriendsMap = world.getFriendsMap();
		Iterator<Entry<String, Friend>> myFriendsMapIterator = myFriendsMap.entrySet()
				.stream().parallel()
				.iterator();
      while (myFriendsMapIterator.hasNext()) {
      	Entry<String, Friend> myFriendEntry = myFriendsMapIterator.next();
      	String myFriendId = myFriendEntry.getKey();
      	Friend myFriend = myFriendEntry.getValue();

      	// include me
      	Node myFriendNode = getOrCreateFriendNodeAndEventuallyBuildEdgesWithMe(myFriend, includeMyUserNode);
//      	if (includeMyUserNode) {
////      		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(myFriendId, egoNode);
//      		myFriendNode = getOrCreateFriendNodeAndEdgesWithMe(friend);
//      	} else {
//      		myFriendNode = createAndMaintainOrRetrieveFriendNode(myFriendId); 
//      	}

         Set<String> mutualFriendsIds = myFriend.getMutualFriendsIds();
//         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         Iterator<String> parallelIterator = mutualFriendsIds.stream().parallel().iterator();
//         forEach(myOtherFriendAlsoMutualWithMeId->{
         while (parallelIterator.hasNext()) {
         	String myOtherFriendAlsoMutualWithMeId = parallelIterator.next();
         	// that is: a cell in myFriendsToMutualFriendsTable contains edge between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
//         		return;
         	}
         	
         	// this creation below could be decrease outer while iterations
//         	Node myOtherFriendAlsoMutualNode = 
//         			createAndMaintainOrRetrieveFriendNode(myOtherFriendAlsoMutualWithMeId);
//         			getOrCreateFriendNode(myOtherFriendAlsoMutualWithMeId);
//         	if (includeMyUserNode)
//         		createAndMaintainOrRetrieveFriendEdgeWithMe(myOtherFriendAlsoMutualNode , egoNode);
//         		getOrCreateFriendNodeAndEdgesWithMe(myFriend);
         	Friend myOtherFriendAlsoMutualWithMe = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         	Node myOtherFriendAlsoMutualNode = getOrCreateFriendNodeAndEventuallyBuildEdgesWithMe(myOtherFriendAlsoMutualWithMe, includeMyUserNode);
         	
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualNode, myfriendwithmutualfriendEdgeUiClass);
         	myFriendsToMutualFriendsEdgesTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);
			}
//         );
      }
      return myFriendsNodesMap.size();
	}
	
	@Override
	protected int createFriendsOfFriendsWithMyFriends() {
//		createMyFriendsOnly();
//		createFriendsOfFriends();
//	}
//	
//	private void createFriendsOfFriends() {
		Iterator<Entry<String, FriendOfFriend>> friendsOfMyFriendsParallelIterator = world.getFriendsOfFriendsMap().entrySet()
				.stream().parallel()
				.iterator();
		while (friendsOfMyFriendsParallelIterator.hasNext()) {
			Entry<String, FriendOfFriend> friendOfMyFriendEntry = friendsOfMyFriendsParallelIterator.next();
			String friendOfMyFriendId = friendOfMyFriendEntry.getKey();
			FriendOfFriend friendOfMyFriend = friendOfMyFriendEntry.getValue();
			Set<String> friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds = friendOfMyFriend.getMutualFriendsIds();

			Node friendOfMyFriendNode = createNode(friendOfMyFriend);
			friendOfMyFriendNode.addAttribute("ui.class", friendOfFriendNodeUiClass);
			friendsOfFriendsNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

			for (String friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds) {
				Node myFriendNode = getOrCreateFriendNode(friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId);
				
				Edge friendOfFriendWithMyFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode, friendofmyfriendwithmyfriendEdgeUiClass);
				friendsOfFriendsWithFriendsEdgesTable.put(friendOfMyFriendsUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, friendOfFriendWithMyFriendEdge);
			}
		}
		
		return friendsOfFriendsNodeMap.size();
	}
	
	@Override
	protected Node createNode(User user) {
		Node node = graph.addNode(user.getUid());
		
		// TODO normalize size using friends number ?
		float normalizedFriendsCount = normalizeFriendsCount( user.getFriendsCount() );
//		Printer.println(user.getUid()+" "+normalizedFriendsCount);
		node.setAttribute("ui.size",  normalizedFriendsCount+"gu");
		
		pause();
		
   	return node;
	}
	
	private float normalizeFriendsCount(int friendCount) {
		// (friendCount - min) / (max - min)
		// min is 1: at least, this user has only my user as friend
		// max is maximum friend number allowed by facebook, that is 4999
		float n = ((float)(friendCount - 1)) / (4999 - 1);
		if (n<0)
			return 0;
		return n;
	}
	
	@Override
	protected Node getOrCreateFriendNodeAndEdgesWithMe(User myFriend) {
		String myFriendId = myFriend.getUid();
		Node myFriendNode = graph.getNode(myFriendId);
		if (myFriendNode!=null)
			return myFriendNode;
		else {
			myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
	      
	      Edge meToMyfriendEdge = createEdge(egoNode, myFriendNode, mewithmyfriendEdgeUiClass);
//	      System.out.println(meToMyfriendEdge.getAttributeKeySet().stream().collect(Collectors.joining(", ")));
	      
	      String egoId = ego.getUid();
	      myFriendsWithMeEdgesAndViceversaTable.put(egoId, myFriendId, meToMyfriendEdge);
	      
	      return myFriendNode;
		}
	}
	
	private Edge createEdge(Node firstNode, Node secondNode, String uiClass) {
		return super.createEdge(firstNode, secondNode, false, InteractionsWeigths.Friendships.FRIEND, uiClass);
	}
	
	protected boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( myFriendsToMutualFriendsEdgesTable.contains(firstUserId, secondUserId) || myFriendsToMutualFriendsEdgesTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}

	@Override
	public void setGraphAsGenerated() {
		graphsHolder.setFriendshipsGraphsCreated(true);
	}
	
	/*private void createAndMaintainOrRetrieveFriendEdgeWithMe(Node myFriendNode, Node egoNode) {
		Edge myFriendWithMeEdge = createEdge(egoNode, myFriendNode);
      myFriendsWithMeEdgesMap.put(myFriendNode.getId(), myFriendWithMeEdge);
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
	}*/
	
//	private Node getOrCreateFriendNode(String friendId) {
//		Node myFriendNode = null;
//		if (myFriendsNodesMap.containsKey(friendId)) {
//			myFriendNode = myFriendsNodesMap.get(friendId);
//			if (graph.getNode(myFriendNode.getId())!=null) {
//				graph.addNode(myFriendNode);
//			}
//		} else {
//			myFriendNode = createNode(friendId);
//			undirectedGraph.addNode(myFriendNode);
//			myFriendsNodesMap.put(friendId,myFriendNode);
//		}
//		return myFriendNode;
//	}
	
//	private boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
//		if ( myFriendsToMutualFriendsTable.contains(firstUserId, secondUserId) || myFriendsToMutualFriendsTable.contains(secondUserId, firstUserId) ) {
//   		return true;
//   	}
//		return false;
//	}

//	public void testGraph() {
//
//		graphNodesCount = graph.getNodeCount();
//		graphEdgesCount = graph.getEdgeCount();
//		
//		System.out.println( "\tmy friends:\n" 
//				+"\t\tnodes (map): "+ myFriendsNodesMap.size()+"\n"
//				+"\t\tedges to me (map): "+myFriendsWithMeEdgesMap.size()+"\n"
//				+"\t\tedges to each others (map): "+myFriendsToMutualFriendsTable.values().size()+"\n"/*" graph:"+graphEdgesCount+"(friendsToMutualFriends+friendsToMe)\n"*/
//				+"\tfriends of my friends (maps):\n"
//				+"\t\tnodes (map): "+friendOfFriendNodeMap.size()+"\n"/*+" graph:"+undirectedGraph.getNodeCount()+"(f+fof+me)\n"*/
//				+"\t\tedges to their/my friends (map): "+friendsOfFriendsWithFriendsTable.size()+"\n"
//				+"\n"
//				+"\tgraph nodes:"+graphNodesCount+"\n"
//				+"\tgraph edges:"+graphEdgesCount+"\n"
//		);
//	}
//	
//
//	public void clear() {
//		egoNode = null;
//		myFriendsNodesMap.clear();
//		myFriendsWithMeEdgesMap.clear();
//		graphNodesCount=0;
//		graphEdgesCount=0;
//		
//		graph.clear();
//		myFriendsToMutualFriendsTable.clear();
//	}

}
