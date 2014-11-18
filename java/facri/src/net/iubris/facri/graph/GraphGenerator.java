package net.iubris.facri.graph;


import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphFactory;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class GraphGenerator {
	
	private final GraphModel graphModel;
	private final Workspace workspace;
	private final GraphFactory factory;
	private final DirectedGraph directedGraph;
	
	private final Map<String, Node> myFriendsNodesMap = new ConcurrentHashMap<>();
	private final Map<String, Edge> myFriendsWithMeEdgesMap = new ConcurrentHashMap<>();
	private final Table<String, String, Edge> friendsToMutualFriendsTable = HashBasedTable.create();
	
	private Map<String, Node> friendOfFriendNodeMap = new ConcurrentHashMap<>();
	private Table<String, String, Edge> friendsOfFriendsWithFriendsTable = HashBasedTable.create();


	public GraphGenerator() {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
      pc.newProject();
      workspace = pc.getCurrentWorkspace();

      //Get a graph model - it exists because we have a workspace
      graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
      
      factory = graphModel.factory();
      
      directedGraph = graphModel.getDirectedGraph();
	}
	
	public void generate(World world) {
		
		Ego ego = world.getMyUser();
		String egoId = ego.getId();
		
		Node egoNode = factory.newNode(egoId);
      egoNode.getNodeData().setLabel(egoId);
      directedGraph.addNode(egoNode);
      
      Iterator<Entry<String, FriendOrAlike>> myFriendsIterator = world.getMyFriendsMap().entrySet().iterator();
      while (myFriendsIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendEntry = myFriendsIterator.next();
      	String friendId = myFriendEntry.getKey();

      	Node myFriendNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(friendId, egoNode);
	         
         FriendOrAlike myFriend = myFriendEntry.getValue();
         Set<String> mutualFriendsIds = myFriend.getMutualFriends();
         
         for (String myOtherFriendAlsoMutualId : mutualFriendsIds) {
         	
         	if ( friendsToMutualFriendsTable.contains(friendId, myOtherFriendAlsoMutualId) || friendsToMutualFriendsTable.contains(myOtherFriendAlsoMutualId, friendId) ) {
         		continue;
         	}
         	
         	// this creation below could be decrease outer while iterations ?
         	Node myOtherFriendAlsoMutualNode = createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(myOtherFriendAlsoMutualId, egoNode);
         	directedGraph.addNode(myOtherFriendAlsoMutualNode);
         	
         	Edge myFriendWithOtherMutualFriendEdge = factory.newEdge(myFriendNode, myOtherFriendAlsoMutualNode, 1f, true);
         	directedGraph.addEdge(myFriendWithOtherMutualFriendEdge);
         	
         	friendsToMutualFriendsTable.put(friendId, myOtherFriendAlsoMutualId, myFriendWithOtherMutualFriendEdge);         	
			}      	
      }
      
      Iterator<Entry<String, FriendOrAlike>> myFriendsOfFriendsIterator = world.getOtherUsersMap().entrySet().iterator();
      while (myFriendsOfFriendsIterator.hasNext()) {
      	Entry<String, FriendOrAlike> myFriendOfFriendEntry = myFriendsOfFriendsIterator.next();
      	String friendOfFriendId = myFriendOfFriendEntry.getKey();
      	
      	FriendOrAlike myFriendOfFriend = myFriendOfFriendEntry.getValue();
         Set<String> friendOfFriendMutualFriendsIdsThatIsMyFriendsIds = myFriendOfFriend.getMutualFriends();
         
         Node friendOfFriendNode = factory.newNode(friendOfFriendId);
         directedGraph.addNode(friendOfFriendNode);         
         friendOfFriendNodeMap.put(friendOfFriendId,friendOfFriendNode);

         for (String friendOfFriendMutualFriendIdThatIsMyFriendId : friendOfFriendMutualFriendsIdsThatIsMyFriendsIds) {
				Node myFriendNode = myFriendsNodesMap.get(friendOfFriendMutualFriendIdThatIsMyFriendId);
				Edge friendOfFriendWithMyFriendEdge = factory.newEdge(myFriendNode, friendOfFriendNode, 1f, true
						);
				directedGraph.addEdge(friendOfFriendWithMyFriendEdge);
				friendsOfFriendsWithFriendsTable.put(friendOfFriendId, friendOfFriendMutualFriendIdThatIsMyFriendId, friendOfFriendWithMyFriendEdge);
			}
      }		
	}
	
	public void exportGraphToGraphML() {
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		Exporter exporterGraphML = ec.getExporter("graphml");     //Get GraphML exporter
		exporterGraphML.setWorkspace(workspace);
		StringWriter stringWriter = new StringWriter();
		ec.exportWriter(stringWriter, (CharacterExporter) exporterGraphML);
//		FileWriter fileWriter;
		try {
			/*fileWriter = */new FileWriter("facri.graphml")
//			.fileWriter
			.write( stringWriter.toString() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Node createNode(String id) {
		Node node = factory.newNode(id);
   	node.getNodeData().setLabel(id);
   	return node;
	}
	
	private Node createAndMaintainOrRetrieveFriendNodeAndEdgeWithMe(String friendId, Node egoNode) {
		if (!myFriendsNodesMap.containsKey(friendId)) {
			Node friendNode = createNode(friendId);
			directedGraph.addNode(friendNode);
	      myFriendsNodesMap.put(friendId, friendNode );
	      Edge myFriendNodeEdge = factory.newEdge(egoNode, friendNode, 1f, true);
	      directedGraph.addEdge(myFriendNodeEdge);
	      myFriendsWithMeEdgesMap.put(friendId, myFriendNodeEdge);
	      return friendNode;
		}
		return myFriendsNodesMap.get(friendId);
	}

}
