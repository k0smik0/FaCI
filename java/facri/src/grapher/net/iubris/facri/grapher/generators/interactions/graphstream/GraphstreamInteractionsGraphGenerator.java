package net.iubris.facri.grapher.generators.interactions.graphstream;


import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.facri.grapher.generators.graphstream.AbstractGraphstreamGraphGenerator;
import net.iubris.facri.grapher.generators.interactions.InteractionsGraphGenerator;
import net.iubris.facri.grapher.generators.interactions.InteractionsWeigths;
import net.iubris.facri.model.graph.GraphsHolder;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.Interactions;
import net.iubris.facri.model.users.User;
import net.iubris.facri.model.world.World;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

@Singleton
public class GraphstreamInteractionsGraphGenerator extends AbstractGraphstreamGraphGenerator implements InteractionsGraphGenerator {
	
//	private final World world;
//	
//	private final Map<String, FriendOrAlike> myFriendsFromWorldMap;
//	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
//	private final Table<String, String, Edge> myFriendsWithMeEdgesAndViceversaTable = HashBasedTable.create();
//	private final Table<String, String, Edge> myFriendsToMutualFriendsEdgesTable = HashBasedTable.create();
//
//	private final Map<String, FriendOrAlike> friendOfFriendFromWorldMap;
//	private final Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
//	private final Table<String, String, Edge> friendsOfFriendsWithFriendsEdgesTable = HashBasedTable.create();
//	
//	private final Graph graph;
//
//	
//	private Ego ego;
//	private Node egoNode;
//	
	private int appreciationsMin;
	private int appreciationsDenominator;
	private int postsCountMin;
	private int postsCountDenominator;
	
	private int interactionsMin;
	private int interactionsDenominator;
//	
//	private int graphSpeed = 3; //ms
//	
//	private final String egoNodeUiClass = "ego";
//	private final String friendNodeUiClass = "friend, all";
//	private final String friendOfFriendNodeUiClass = "friendof";
//	
//	private final String meToMyfriendEdgeUiClass = "metomyfriend";
//	private final String myfriendToMeEdgeUiClass = "myfriendtome";
//	private final String myfriendWithMutualFriendEdgeUiClass = "myfriendwithmutualfriend";
//	private final String friendofmyfriendWithmyfriendEdgeUiClass = "friendofmyfriendwithmyfriend";
//
//	private final GraphsHolder graphsHolder;
	

	@Inject
	public GraphstreamInteractionsGraphGenerator(GraphsHolder graphsHolder, World world) {
		super(graphsHolder, graphsHolder.getInteractionsGraph(), world);
		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
//		this.graphsHolder = graphsHolder;
//		this.graph = 
////				new MultiGraph("Interactions",false,true);
//				graphsHolder.getInteractionsGraph();
//		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
////		MultiGraph multiGraph = new MultiGraph("a");
//		this.world = world;
//		this.myFriendsFromWorldMap = world.getMyFriendsMap();
//		this.friendOfFriendFromWorldMap = world.getOtherUsersMap();
	}	

	private void retrieveDataForNormalizations() {
		Set<Integer> postsCountRange = world.getPostsCountRange();
		postsCountMin = Collections.min(postsCountRange);
		postsCountDenominator = Collections.max(postsCountRange)-postsCountMin;
//System.out.print("["+postsCountMin+", "+postsCountDenominator+"] ");
		
		Set<Integer> appreciationsRange = world.getAppreciationsRange();
		appreciationsMin = Collections.min(appreciationsRange);
		// it is weighted, so multiplicate for maximum weight
		appreciationsDenominator = InteractionsWeigths.RESHARED_OWN_POST*Collections.max(appreciationsRange)-appreciationsMin;
//System.out.print("["+appreciationsMin+", "+appreciationsDenominator+"] ");
		
		Set<Integer> interactionsRange = world.getInteractionsRange();
		interactionsMin = Collections.min(interactionsRange);
		// it is weighted, so multiplicate for maximum weight
		interactionsDenominator = InteractionsWeigths.POST_TO*Collections.max(interactionsRange)-interactionsMin;
//System.out.print("["+interactionsMin+", "+interactionsDenominator+"] ");
	}
	
//	public void prepareForDisplay() {
//		graph.addAttribute("ui.quality");
//		graph.addAttribute("ui.antialias");
//		graph.display();
//		
//		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//		Viewer viewer = new Viewer(graph,  Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
////		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
//		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
//		viewer.enableAutoLayout();
//	}
	
//	public void reparseGraphCSS() {
//		
//		graph.setAttribute("ui.stylesheet", "");
//		graph.setAttribute("ui.stylesheet", "url('interactions.css')");
////		Viewer viewer = graph.display();
////		viewer.enableAutoLayout();
//	}
	
	@Override
	public void generateMyFriends() {
		retrieveDataForNormalizations();
		super.generateMyFriends();
	}
	
	@Override
	public void generateMeWithMyFriends() {
		retrieveDataForNormalizations();
		super.generateMeWithMyFriends();
//		testGraph();
//		garbageUselessFriendsOfFriends();
	}
	
	@Override
	public void generateMyFriendsAndFriendOfFriends() {
		retrieveDataForNormalizations();
		super.generateMyFriendsAndFriendOfFriends();
	}
	
	// TODO inherit from abstractEtc, inserting retrieveDataEtc
//	@Override
//	public void generateFriendOfFriends() {
//		retrieveDataForNormalizations();
//		super.generateFriendOfFriends();
//	}
	
	@Override
	public void generateFriendOfFriends() {
		retrieveDataForNormalizations();
		createFriendsOfFriendsWithMyFriends();
		for (Node myfriendNode: myFriendsNodesMap.values())
			graph.removeNode(myfriendNode);
	}
	
	// TODO inherit from abstractEtc, inserting retrieveDataEtc
	@Override
	public void generateMeWithMyFriendsAndTheirFriends() {
		retrieveDataForNormalizations();
		super.generateMeWithMyFriendsAndTheirFriends();
	}

	@Override
	protected void createMyFriends(boolean includeMyuserNode) {
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
      	final Node myFriendNode = getOrCreateFriendNodeAndEventuallyEdgesWithMe(myFriend, includeMyuserNode);
//      	myFriendNode.addAttribute("ui.class", friendNodeUiClass);
//      	myFriendNode.addAttribute("stroke-width","10px");
	         
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
         for (String myOtherFriendAlsoMutualWithMeId: mutualFriendsIds) {
//         mutualFriendsIds.stream().forEach( new Consumer<String>() {
//				@Override
//				public void accept(String myOtherFriendAlsoMutualWithMeId) {
         	
//         	// that is: a cell in myFriendsToMutualFriendsTable contains edges between these my friends 
         	if (areMutualFriendsAlreadyComputed(myFriendId, myOtherFriendAlsoMutualWithMeId)) {
         		continue;
         	}
         	
         	// this below could be minimize outer while iterations
//         	Node myOtherFriendAlsoMutualFriendNode = getOrCreateFriendNode(myOtherFriendAlsoMutualWithMeId);
//         	if (includeMyuserNode) {
//         		// my mutual friend to me and viceversa
//         		FriendOrAlike myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
//         		getOrCreateFriendNodeAndEdgesWithMe(myOtherFriendAlsoMutual);
//         	}
//         	myOtherFriendAlsoMutualFriendNode.addAttribute("ui.class", friendNodeUiClass);
         	
         	FriendOrAlike myOtherFriendAlsoMutual = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId);
         	Node myOtherFriendAlsoMutualFriendNode = getOrCreateFriendNodeAndEventuallyEdgesWithMe(myOtherFriendAlsoMutual, includeMyuserNode);
         	
         	
         	Interactions myFriendToMutualFriendInteractions = myFriend.getToOtherUserInteractions(myOtherFriendAlsoMutualWithMeId);
         	float myFriendToMytualFriendInteractions = 
//         			myFriend.getToOtherUserInteractionsCount(myOtherFriendAlsoMutualWithMeId);
         			getWeightedInteractions(myFriendToMutualFriendInteractions);
         	
         	float myFriendToMytualFriendNormalizedInteractionsValue = ((float)myFriendToMytualFriendInteractions-interactionsMin)/interactionsDenominator;
//         	float myFriendToMytualFriendNormalizedInteractions = (myFriendToMytualFriendInteractions>0) ? s(float)myFriendToMytualFriendInteractions : 0;
//         	System.out.println(myFriendToMytualFriendInteractions+" -> "+myFriendToMytualFriendNormalizedInteractions);
         	Edge myFriendWithOtherMutualFriendEdge = createEdge(myFriendNode, myOtherFriendAlsoMutualFriendNode, myFriendToMytualFriendNormalizedInteractionsValue, myfriendWithMutualFriendEdgeUiClass);
         	myFriendsToMutualFriendsEdgesTable.put(myFriendId, myOtherFriendAlsoMutualWithMeId, myFriendWithOtherMutualFriendEdge);
         	
         	
         	Interactions mutualFriendToMyFriendInteractions = myFriendsMap.get(myOtherFriendAlsoMutualWithMeId).getToOtherUserInteractions(myFriendId);
         	float myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsValue = 
//         			myFriendsMap.get(myOtherFriendAlsoMutualWithMeId).getToOtherUserInteractionsCount(myFriendId);
         			getWeightedInteractions(mutualFriendToMyFriendInteractions);
         	float myOtherFriendAlsoMutualWithMeToMyFriendSubjectNormalizedInteractionsValue = ((float)myOtherFriendAlsoMutualWithMeToMyFriendSubjectInteractionsValue-interactionsMin)/interactionsDenominator;
         	Edge myOtherFriendAlsoMutualWithMeToMyFriendSubjectEdge = createEdge(myOtherFriendAlsoMutualFriendNode, myFriendNode, myOtherFriendAlsoMutualWithMeToMyFriendSubjectNormalizedInteractionsValue, myfriendWithMutualFriendEdgeUiClass);
         	myFriendsToMutualFriendsEdgesTable.put(myOtherFriendAlsoMutualWithMeId, myFriendId, myOtherFriendAlsoMutualWithMeToMyFriendSubjectEdge);
         	
         	
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
	
	private float getWeightedInteractions(Interactions toOtherUserInteractions) {
		return toOtherUserInteractions.getLikesCount()*InteractionsWeigths.POST_LIKE
		+toOtherUserInteractions.getCommentsCount()*InteractionsWeigths.POST_COMMENT
		+toOtherUserInteractions.getTagsCount()*InteractionsWeigths.TAG
		+toOtherUserInteractions.getPostsCount()*InteractionsWeigths.POST_TO;
	}
	
	@Override
	protected void createFriendsOfFriendsWithMyFriends() {
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

			for (String mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId: friendOfMyFriendMutualFriendsIdsThatIsMyFriendsIds ) {
				Node myFriendNode = getOrCreateFriendNode(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId);
				myFriendNode.addAttribute("ui.class", friendNodeUiClass);

				// TODO normalize
				Interactions myfriendToHimFriendInteractions = myFriendsFromWorldMap.get(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId).getToOtherUserInteractions(friendOfMyFriendId);
				float myfriendToHimFriendInteractionsAsWeight = 
//						myFriendsFromWorldMap.get(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId).getToOtherUserInteractionsCount(friendOfMyFriendId);
					getWeightedInteractions(myfriendToHimFriendInteractions);
//				System.out.println(myfriendToHimFriendInteractionsAsWeight);
				float myfriendToHimFriendInteractionsAsNormalizedWeight = ((float)myfriendToHimFriendInteractionsAsWeight-interactionsMin)/interactionsDenominator;
		      Edge myFriendWithHimFriendEdge = createEdge(myFriendNode, friendOfMyFriendNode, myfriendToHimFriendInteractionsAsNormalizedWeight, friendofmyfriendWithmyfriendEdgeUiClass);
		      friendsOfFriendsWithFriendsEdgesTable.put(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId, friendOfMyFriendId, myFriendWithHimFriendEdge);
		      
		      Interactions friendOfFriendUserToMyFriendInteractions = friendOfMyFriend.getToOtherUserInteractions(mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId);
		      float friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractionsValue = 
//		      		friendOfMyFriend.getToOtherUserInteractionsCount(friendOfFriendUserIdThatIsMyFriendThatIsMutualFriendId);
	      		getWeightedInteractions(friendOfFriendUserToMyFriendInteractions);
//		      System.out.println(friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractions);
		      float friendOfMyFriendToHimFrienThatIsMutualFriendWithMeNormalizedInteractionsValue = ((float)friendOfMyFriendToHimFrienThatIsMutualFriendWithMeInteractionsValue-interactionsMin)/interactionsDenominator;
				Edge friendOfMyFriendToHimFrienThatIsMutualFriendEdge = createEdge(friendOfMyFriendNode,myFriendNode,friendOfMyFriendToHimFrienThatIsMutualFriendWithMeNormalizedInteractionsValue, friendofmyfriendWithmyfriendEdgeUiClass);
				friendsOfFriendsWithFriendsEdgesTable.put(friendOfMyFriendId, mutualFriendWithFriendOfMyfriendThatIsMyFrienduserId, friendOfMyFriendToHimFrienThatIsMutualFriendEdge);
			}
		}
	}
	
/*	// inherit from AbstractEtc
	private Node getOrCreateFriendNode(User myFriend, boolean includeMyuserNode) {
		Node myFriendNode = null;
		if (includeMyuserNode) {
   		myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgesWithMe(myFriend);
   	} else {
   		myFriendNode = getOrCreateFriendNode(myFriend.getUid());
   	}
		return myFriendNode;
	}

	// inherit from AbstractEtc
	private Node getOrCreateFriendNode(String myFriendId) {
		FriendOrAlike myFriend = myFriendsFromWorldMap.get(myFriendId);
		Node myFriendNode = graph.getNode(myFriendId); 
		if (myFriendNode==null) {
			myFriendNode = createNode(myFriend);
			myFriendsNodesMap.put(myFriendId, myFriendNode);
		}
		return myFriendNode;
	}*/
	
	@Override
	protected Node getOrCreateFriendNodeAndEdgesWithMe(User myFriend) {
		String myFriendId = myFriend.getUid();
		Node myFriendNode = graph.getNode(myFriendId);
		if (myFriendNode!=null)
			return myFriendNode;
		else {			
			myFriendNode = createNode(myFriend);
	      myFriendsNodesMap.put(myFriendId, myFriendNode);
	      
	      Interactions meToMyfriendInteractions = ego.getToOtherUserInteractions(myFriendId);
	      float meToFriendInteractionsAsWeight = 
	      		//ego.getToOtherUserInteractionsCount(myFriendId);
	      		getWeightedInteractions(meToMyfriendInteractions);
	      float mineToMyFriendNormalizedInteractions = ((float)meToFriendInteractionsAsWeight-interactionsMin)/interactionsDenominator;
	      Edge meToMyfriendEdge = createEdge(egoNode, myFriendNode, mineToMyFriendNormalizedInteractions, meToMyfriendEdgeUiClass);
	      
	      Interactions myfriendToMeInteractions = myFriend.getToOtherUserInteractions(ego.getUid());
	      float myfriendToMeInteractionsAsWeight = 
//	      		myFriend.getToOtherUserInteractionsCount(myFriendId);
	      		getWeightedInteractions(myfriendToMeInteractions);
	      float myFriendToMeNormalizedInteractions = ((float)myfriendToMeInteractionsAsWeight-interactionsMin)/interactionsDenominator;
//	      float myFriendToMeNormalizedInteractions  = (myfriendToMeInteractionsAsWeight >0) ? 1/(float)myfriendToMeInteractionsAsWeight  : 0;
//	      System.out.println(myfriendToMeInteractionsAsWeight+" -> "+myFriendToMeNormalizedInteractions);
	      Edge myFriendToMeEdge = createEdge(myFriendNode, egoNode, myFriendToMeNormalizedInteractions, myfriendToMeEdgeUiClass);
//	      graph.removeEdge(meToMyfriendEdge);
//	      graph.removeEdge(myFriendToMeEdge);
	      
	      String egoId = ego.getUid();
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
	
	// implements on AbstractEtc
	@Override
	protected Node createNode(User user) {
		String id = user.getUid();
		Node node = graph.addNode(id);
		
		float normalizedSize = ((float)user.getOwnPostsOnOwnWallCount()-postsCountMin)/postsCountDenominator;
		// use size for posts count
//		System.out.println(id+" "+normalizedSize);
		node.setAttribute("ui.size", normalizedSize+"gu");
		// use color for posts count
//		node.setAttribute("ui.color", normalizedSize);
		
		int appreciation = user.getOwnLikedPostsCount() + InteractionsWeigths.RESHARED_OWN_POST*user.getOwnPostsResharingCount();
		float normalizedAppreciation = ((float)appreciation-appreciationsMin)/appreciationsDenominator;
		// use color for posts appreciations
		node.setAttribute("ui.color", normalizedAppreciation);
		// use size for posts appreciations
//		String s = normalizedAppreciation+"px";
//		System.out.println(id+" "+s);
//		node.setAttribute("ui.size", s);
		
		pause(graphSpeed );

		return node;
	}
	
	// implements on AbstractEtc
	@Override
	protected Edge createEdge(Node firstNode, Node secondNode, boolean directed, float normalizedWeight, String uiClass) {
		/*
		String edgeId = firstNode.getId()+"_to_"+secondNode.getId();
//		Edge createdEdge = graph.addEdge(edgeId, firstNode, secondNode, true);
		Edge createdEdge = graph.addEdge(edgeId, firstNode.getId(), secondNode.getId(), true);
		
		createdEdge.setAttribute("ui.class", uiClass);
//		createdEdge.setAttribute("ui.arrow-size", weight);
//		float color = Math.abs(weight*1);
		*/
		
	// the higher weight, the longer/larger/coloured, so we complement
		Edge createdEdge = super.createEdge(firstNode, secondNode, directed, normalizedWeight, uiClass);
		float complementarWeight = 1-normalizedWeight;
		createdEdge.setAttribute("ui.color", complementarWeight);
		createdEdge.setAttribute("ui.size", /*2**/complementarWeight);
		createdEdge.setAttribute("interactions", complementarWeight);
//		createdEdge.setAttribute("ui.arrow-size", normalizedWeight);
//		System.out.print(createdEdge.getId()+" ");
		
		super.pause();
		
   	return createdEdge;
	}
	
	
	private Edge createEdge(Node firstNode, Node secondNode, float normalizedWeight, String uiClass) {
		return createEdge(firstNode, secondNode, true, normalizedWeight,uiClass);
	}
	
	@Override
	public void setGraphAsGenerated() {
		graphsHolder.setInteractionsGraphsCreated(true);
	}

	@Override
	public void testGraph() {
		super.testGraph();
	}

	@Override
	protected boolean areMutualFriendsAlreadyComputed(String firstUserId, String secondUserId) {
		if ( myFriendsToMutualFriendsEdgesTable.contains(firstUserId, secondUserId) && myFriendsToMutualFriendsEdgesTable.contains(secondUserId, firstUserId) ) {
   		return true;
   	}
		return false;
	}
	
}
