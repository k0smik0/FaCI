/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GraphstreamInteractionsGraphGenerator.java) is part of facri.
 * 
 *     GraphstreamInteractionsGraphGenerator.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GraphstreamInteractionsGraphGenerator.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.grapher.generators.specialized.interactions.graphstream;


import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.faci.grapher.generators.base.InteractionsWeigths;
import net.iubris.faci.grapher.generators.base.graphstream.AbstractGraphstreamGraphGenerator;
import net.iubris.faci.grapher.generators.specialized.interactions.InteractionsGraphGenerator;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.model.world.users.Interactions;
import net.iubris.faci.model.world.users.User;
import net.iubris.faci.utils.Pauser;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import com.google.common.collect.Table;

@Singleton
public class GraphstreamInteractionsGraphGenerator extends AbstractGraphstreamGraphGenerator implements InteractionsGraphGenerator {
	
	private final String metomyfriendEdgeUiClass = "me_to_my_friend";
	private final String myfriendtomeEdgeUiClass = "my_friend_to_me";
	private final String myfriendtomutualfriendEdgeUiClass = "my_friend_to_mutual_friend";
	private final String friendofmyfriendtomyfriendEdgeUiClass = "friend_of_my_friend_to_my_friend";
		
	private float appreciationsMin;
	private float appreciationsDenominator;
	private int postsCountMin;
	private int postsCountDenominator;
	
	private int interactionsMin;
	private int interactionsDenominator;
	

	@Inject
	public GraphstreamInteractionsGraphGenerator(GraphsHolder graphsHolder, World world) {
		super(graphsHolder, graphsHolder.getInteractionsGraph(), world);
		graph.setAttribute("ui.stylesheet", "url('css"+File.separatorChar+"interactions.css')");
		graph.setAttribute("ui.title", "Interactions");
	}	
	
	@Override
	public void generateMeWithMyFriends() {
		retrieveDataForNormalizations();
		super.generateMeWithMyFriends();
//		testGraph();
//		garbageUselessFriendsOfFriends();
	}
	
	@Override
	public void generateMeWithMyFriendsAndFriendsOfFriends() {
		retrieveDataForNormalizations();
		super.generateMeWithMyFriendsAndFriendsOfFriends();
	}
	
	@Override
	public void generateMyFriends() {
		retrieveDataForNormalizations();
		super.generateMyFriends();
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		retrieveDataForNormalizations();
		super.generateMyFriendsAndFriendOfFriends();
	}
	
//	@Override
//	public void generateFriendOfFriends() {
//		retrieveDataForNormalizations();
//		createFriendsOfFriendsWithMyFriends();
//		for (Node myfriendNode: myFriendsNodesMap.values())
//			graph.removeNode(myfriendNode);
//	}

	@Override
	protected int createMyFriends(boolean includeMyuserNode) {
		Map<String, Friend> myFriendsMap = world.getFriendsMap();
		Iterator<Entry<String, Friend>> myFriendsMapParallelIterator = myFriendsMap.entrySet().stream().parallel().iterator();
		while (myFriendsMapParallelIterator.hasNext()) {
      	Entry<String, Friend> myFriendEntry = myFriendsMapParallelIterator.next();
   			
      	String myFriendId = myFriendEntry.getKey();
      	Friend myFriend = myFriendEntry.getValue();

      	// eventually, creates edges with me 
      	final Node myFriendNode = getOrCreateFriendNodeAndEventuallyBuildEdgesWithMe(myFriend, includeMyuserNode);
//System.out.println(""+myFriendNode.getAttribute("ui.size"));
	         
         Set<String> mutualFriendsIds = myFriend.getMutualFriendsIds();
//         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
         Iterator<String> parallelIterator = mutualFriendsIds.stream().parallel().iterator();
         while (parallelIterator.hasNext()) {
         	
         	String myOtherFriendAlsoMutualWithMeId = parallelIterator.next();
//         	// that is: a cell in myFriendsToMutualFriendsTable contains edges between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
         	
         	// this below could be minimize outer while iterations
         	Friend myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         	Node myOtherFriendAlsoMutualFriendNode = getOrCreateFriendNodeAndEventuallyBuildEdgesWithMe(myOtherFriendAlsoMutual, includeMyuserNode);
//System.out.println(""+myOtherFriendAlsoMutualFriendNode.getAttribute("ui.size"));
         	
         	Interactions myFriendToMutualFriendInteractions = myFriend.getToOtherUserInteractions(myOtherFriendAlsoMutualWithMeId);
         	float myFriendToMutualFriendInteractionsAsWeight = getWeightedInteractions(myFriendToMutualFriendInteractions);
         	if (myFriendToMutualFriendInteractionsAsWeight>0) {
	         	float myfriendToMytualFriendInteractionsAsWeightNormalized = normalizeInteractions(myFriendToMutualFriendInteractionsAsWeight);
	//         	System.out.println(myFriendToMytualFriendInteractions+" -> "+myFriendToMytualFriendNormalizedInteractions);
	         	createEdge(myFriendNode, myOtherFriendAlsoMutualFriendNode, 
         			myFriendToMutualFriendInteractionsAsWeight,
         			myfriendToMytualFriendInteractionsAsWeightNormalized,
         			myfriendtomutualfriendEdgeUiClass,
         			myFriendsToMutualFriendsEdgesTable, myFriendId, myOtherFriendAlsoMutualWithMeId
      			);
         	}
         	myFriendsToMutualFriendsEdgesComputedTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, true);
         	
         	Interactions mutualFriendToMyFriendInteractions = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId).getToOtherUserInteractions(myFriendId);
         	float myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeight = getWeightedInteractions(mutualFriendToMyFriendInteractions);
         	if (myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeight>0) {
	         	float myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeightNormalized = normalize(myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeight, interactionsMin, interactionsDenominator);
      			createEdge(myOtherFriendAlsoMutualFriendNode, myFriendNode, 
         			myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeight,
         			myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsAsWeightNormalized,
         			myfriendtomutualfriendEdgeUiClass, 
         			myFriendsToMutualFriendsEdgesTable, myOtherFriendAlsoMutualWithMeId, myFriendId
      			);
         	}
         	myFriendsToMutualFriendsEdgesComputedTable.put(myOtherFriendAlsoMutualWithMeId, myFriendId, true);
         	
         	
//				// one edge, with weight as difference
//		      float diff = myfriendToHimFriendInteractionsAsWeight - friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions;
//		      Edge edge = graph.addEdge(myFriendNode.getId()+"_to_"+egoNode.getId(), myFriendNode, egoNode, true);
//		      edge.setAttribute("ui.size", 1-diff);
//		      edge.setAttribute("ui.color", diff);
//		      edge.setAttribute("ui.class", friendofmyfriendWithmyfriendEdgeUiClass);
//		      myFriendsWithMeEdgesAndViceversaTable.put(friendOfMyFriendId, friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, edge);
//		      myFriendsWithMeEdgesAndViceversaTable.put(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId, friendOfMyFriendId, edge);
			}
      }
		return myFriendsNodesMap.size();
	}
	
	@Override
	protected int createFriendsOfFriendsWithMyFriends() {
//		Map<String, FriendOfFriend> friendsOfFriendsMap = world.getFriendsOfFriendsMap();
		Iterator<Entry<String, FriendOfFriend>> friendsOfMyFriendsParallelIterator = friendsOfFriendsFromWorldMap.entrySet().stream().parallel().iterator();
		while (friendsOfMyFriendsParallelIterator.hasNext()) {
			Entry<String, FriendOfFriend> friendOfMyFriendEntry = friendsOfMyFriendsParallelIterator.next();
			String friendOfMyFriendId = friendOfMyFriendEntry.getKey();

			FriendOfFriend friendOfMyFriend = friendOfMyFriendEntry.getValue();
			Set<String> friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds = friendOfMyFriend.getMutualFriendsIds();

			Node friendOfMyFriendNode = createNode(friendOfMyFriend);
			friendOfMyFriendNode.addAttribute("ui.class", friendOfFriendNodeUiClass );
			friendsOfFriendsNodeMap.put(friendOfMyFriendId, friendOfMyFriendNode);

//			for (String mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds ) {
			Iterator<String> parallelIterator = friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds.stream().parallel().iterator();
			while (parallelIterator.hasNext()) {
				String mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId = parallelIterator.next();
				Node myFriendNode = getOrCreateFriendNode(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId);
				myFriendNode.addAttribute("ui.class", friendNodeUiClass);
//System.out.println(""+myFriendNode.getAttribute("ui.size"));

				// TODO new check
				// friend -> friend of friend ?! not available data ! why this ?!
				Interactions myfriendToHisFriendInteractions = myFriendsFromWorldMap.get(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId).getToOtherUserInteractions(friendOfMyFriendId);
				float myfriendToHimFriendInteractionsAsWeight = getWeightedInteractions(myfriendToHisFriendInteractions);
//System.out.println(myfriendToHimFriendInteractionsAsWeight);
				if (myfriendToHimFriendInteractionsAsWeight>0) {
	//				System.out.println(myfriendToHimFriendInteractionsAsWeight);
					float myfriendToHimFriendInteractionsAsWeightNormalized = normalizeInteractions(myfriendToHimFriendInteractionsAsWeight);
//System.out.println("f: "+myfriendToHimFriendInteractionsAsWeightNormalized);
					createEdge(myFriendNode, friendOfMyFriendNode, 
						myfriendToHimFriendInteractionsAsWeight,
						myfriendToHimFriendInteractionsAsWeightNormalized,
						friendofmyfriendtomyfriendEdgeUiClass,
						friendsOfFriendsWithFriendsEdgesTable, mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId, friendOfMyFriendId
					);
				}
				friendsOfFriendsWithFriendsEdgesComputedTable.put(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId, friendOfMyFriendId, true);
		      
				// friend of friend -> friend
		      Interactions friendOfFriendUserToMyFriendInteractions = friendOfMyFriend.getToOtherUserInteractions(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId);
		      float friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeight = getWeightedInteractions(friendOfFriendUserToMyFriendInteractions);
		      if (friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeight>0) {
	//		      System.out.println(friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions);
			      float friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeightNormalized = normalizeInteractions(friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeight);
//System.out.println("fof: "+friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeightNormalized+"\n");
	      		createEdge(friendOfMyFriendNode, myFriendNode, 
		      		friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeight,
		      		friendOfMyfriendToHimFrienThatIsMutualFriendWithMeInteractionsAsWeightNormalized,
		      		friendofmyfriendtomyfriendEdgeUiClass,
		      		friendsOfFriendsWithFriendsEdgesTable, friendOfMyFriendId, mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId
	      		);
		      }
		      friendsOfFriendsWithFriendsEdgesComputedTable.put(friendOfMyFriendId, mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId, true);
			}
		}
		return friendsOfFriendsNodeMap.size();
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
	      
	      String egoId = ego.getUid();
	      
	      if (!myFriendsWithMeEdgesAndViceversaComputedTable.contains(egoId, myFriendId)) {
		      Interactions meToMyfriendInteractions = ego.getToOtherUserInteractions(myFriendId);
		      float meToMyfriendInteractionsAsWeight = getWeightedInteractions(meToMyfriendInteractions);
//		      Edge meToMyfriendEdge = null;
		      if (meToMyfriendInteractionsAsWeight>0) {
			      float meToMyfriendInteractionsAsWeightNormalized = normalizeInteractions(meToMyfriendInteractionsAsWeight);
//			      if (graph.getEdge(buildEdgeId(egoNode, myFriendNode))==null)
			      	createEdge(egoNode, myFriendNode, 
		      			meToMyfriendInteractionsAsWeight,
		      			meToMyfriendInteractionsAsWeightNormalized,
		      			metomyfriendEdgeUiClass,
		      			myFriendsWithMeEdgesAndViceversaTable, egoId, myFriendId
	      			);
		      }
		      myFriendsWithMeEdgesAndViceversaComputedTable.put(egoId, myFriendId, true);
	      }
	      
	      if (!myFriendsWithMeEdgesAndViceversaComputedTable.contains(myFriendId, egoId)) {
		      Interactions myfriendToMeInteractions = myFriend.getToOtherUserInteractions(egoId);
		      float myfriendToMeInteractionsAsWeight = getWeightedInteractions(myfriendToMeInteractions);
//		      Edge myfriendToMeEdge = null;
		      if (myfriendToMeInteractionsAsWeight>0) {
			      float myfriendToMeInteractionsAsWeightNormalized = normalizeInteractions(myfriendToMeInteractionsAsWeight);
//			      if (graph.getEdge(buildEdgeId(myFriendNode, egoNode))==null)
			      	createEdge(myFriendNode, egoNode, 
		      			myfriendToMeInteractionsAsWeight,
		      			myfriendToMeInteractionsAsWeightNormalized,
		      			myfriendtomeEdgeUiClass,
		      			myFriendsWithMeEdgesAndViceversaTable, myFriendId, egoId
	      			);
		      }
		      myFriendsWithMeEdgesAndViceversaComputedTable.put(myFriendId, egoId, true);
	      }
	      
//	      // experimental: one edge, with weight as difference	      
//	      float diff = () meToMyFriendNormalizedInteractions - myFriendToMeNormalizedInteractions;
//	      Edge edge = graph.addEdge(myFriendNode.getId()+"_to_"+egoNode.getId(), myFriendNode, egoNode, true);
//	      edge.setAttribute("ui.size", 1-diff);
//	      edge.setAttribute("ui.color", diff);
//	      edge.setAttribute("ui.class", myfriendWithMeEdgeUiClass);
//	      myFriendsWithMeEdgesAndViceversaTable.put(ego.getId(), myFriendId, edge);
//	      myFriendsWithMeEdgesAndViceversaTable.put(myFriendId, ego.getId(), edge);
	      
	      return myFriendNode;
		}
	}
	
	// implements on AbstractEtc
	@Override
	protected Node createNode(User user) {
		String id = user.getUid();
		Node node = graph.addNode(id);
		
		float normalizedSize = normalize(user.getOwnPostsOnOwnWallCount(),postsCountMin,postsCountDenominator);
		// use size for posts count
//		System.out.println(id+" "+normalizedSize);
		node.setAttribute("ui.size", normalizedSize+"gu");
		// use color for posts count
//		node.setAttribute("ui.color", normalizedSize);
		
		float appreciation = user.getOwnLikedPostsCount() + InteractionsWeigths.Interactions.RESHARED_OWN_POST*user.getOwnPostsResharingCount();
		float normalizedAppreciation = normalize(appreciation, appreciationsMin, appreciationsDenominator);
		// use color for posts appreciations
		node.setAttribute("ui.color", normalizedAppreciation);
		// use size for posts appreciations
//		String s = normalizedAppreciation+"px";
//		System.out.println(id+" "+s);
//		node.setAttribute("ui.size", s);
		
		Pauser.sleep(graphSpeed );

		return node;
	}
	
	private Edge createEdge(Node firstNode, Node secondNode, float realWeight, float normalizedWeight, String uiClass, Table<String, String, Edge> edgesTable, String firstNodeId, String secondNodeId) {
		Edge createdEdge = createEdge(firstNode, secondNode, realWeight, normalizedWeight, uiClass);
		edgesTable.put(firstNodeId, secondNodeId, createdEdge);
		return createdEdge;
	}
	
	private Edge createEdge(Node firstNode, Node secondNode, float realWeight, float normalizedWeight, String uiClass) {
				
		Edge	createdEdge = super.createEdge(firstNode, secondNode, true, normalizedWeight, uiClass);
		createdEdge.setAttribute("weight", realWeight);
		
		// the larger the value, the more far, so complement
		float complementarWeight = 1-normalizedWeight;
		

//	createdEdge.setAttribute("ui.color", realWeight);
		createdEdge.setAttribute("ui.color", 3*normalizedWeight); // just for visual experience
//		createdEdge.setAttribute("ui.color", complementarWeight);
		
//			createdEdge.setAttribute("ui.size", normalizedWeight);
		createdEdge.setAttribute("layout.weight", complementarWeight);

//		createdEdge.setAttribute("layout.weight", normalizedWeight);
		
		createdEdge.addAttribute("ui.interactions", realWeight); // for mousemanager
		
		super.pause();
		
   	return createdEdge;
	}
	
	@Override
	protected boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( 	myFriendsToMutualFriendsEdgesComputedTable.contains(firstUserId, secondUserId) 
				&& 
				myFriendsToMutualFriendsEdgesComputedTable.contains(secondUserId, firstUserId) 
			) {
   		return true;
   	}
		return false;
	}
	
	private void retrieveDataForNormalizations() {
		Set<Integer> postsCountRange = world.getPostsCountRange();
		postsCountMin = Collections.min(postsCountRange);
		postsCountDenominator = Collections.max(postsCountRange)-postsCountMin;
//System.out.print("["+postsCountMin+", "+postsCountDenominator+"] ");
		
		Set<Float> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = Collections.min(appreciationsRange);
		// it is weighted, so multiplicate for maximum weight
		appreciationsDenominator = InteractionsWeigths.Interactions.RESHARED_OWN_POST*Collections.max(appreciationsRange)-appreciationsMin;
//System.out.print("["+appreciationsMin+", "+appreciationsDenominator+"] ");
		
		Set<Integer> interactionsRange = world.getInteractionsRange();
		interactionsMin = Collections.min(interactionsRange);
		// it is weighted, so multiplicate for maximum weight
		interactionsDenominator = net.iubris.faci.grapher.generators.base.InteractionsWeigths.Interactions.POST_TO_WALL*Collections.max(interactionsRange)-interactionsMin;
//System.out.print("["+interactionsMin+", "+interactionsDenominator+"] ");
	}
	
	private float getWeightedInteractions(Interactions toOtherUserInteractions) {
		return 
				toOtherUserInteractions.getLikesCount()	*InteractionsWeigths.Interactions.POST_LIKE
			  +toOtherUserInteractions.getCommentsCount()*InteractionsWeigths.Interactions.POST_COMMENT
			  +toOtherUserInteractions.getTagsCount()		*InteractionsWeigths.Interactions.TAG
			  +toOtherUserInteractions.getPostsCount()	*InteractionsWeigths.Interactions.POST_TO_WALL ;
	}
	
	private float normalize(float value, float min, float denominator) {
		return (value-min)/denominator;
	}
	
	private float normalizeInteractions(float value) {
		return normalize(value,interactionsMin,interactionsDenominator);
	}
	
	@Override
	public void setGraphAsGenerated() {
		graphsHolder.setInteractionsGraphsCreated(true);
	}
	
}
