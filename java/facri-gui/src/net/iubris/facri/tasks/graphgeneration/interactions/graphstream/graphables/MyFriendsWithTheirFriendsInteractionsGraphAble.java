package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables;

import javax.inject.Inject;

import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.ishtaran.task.phasable.CallState;

public class MyFriendsWithTheirFriendsInteractionsGraphAble extends AbstractInteractionsGraphstreamGraphAble {
	
	@Inject
	public MyFriendsWithTheirFriendsInteractionsGraphAble(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		super(graphstreamInteractionsGraphGenerator);
	}
	
	@Override
	public CallState call() throws Exception {
		graphstreamInteractionsGraphGenerator.clear();
		graphstreamInteractionsGraphGenerator.prepareForDisplay();
		graphstreamInteractionsGraphGenerator.generateMyFriendsAndFriendOfFriends();
		return callState;
	}
}
