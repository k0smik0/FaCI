package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables;

import javax.inject.Inject;

import net.iubris.faci.grapher.generators.specialized.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.ishtaran.task.phasable.CallState;

public class MyFriendsWithMeInteractionsGraphAble extends AbstractInteractionsGraphstreamGraphAble {
	
	@Inject
	public MyFriendsWithMeInteractionsGraphAble(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		super(graphstreamInteractionsGraphGenerator);
	}
	
	@Override
	public CallState call() throws Exception {
		graphstreamInteractionsGraphGenerator.clear();
		graphstreamInteractionsGraphGenerator.prepareForDisplay();
		graphstreamInteractionsGraphGenerator.generateMeWithMyFriends();
		return callState;
	}
}
