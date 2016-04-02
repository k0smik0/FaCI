package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables;

import javax.inject.Inject;

import net.iubris.faci.grapher.generators.specialized.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.faci.grapher.holder.core.GraphsHolder;
import net.iubris.ishtaran.task.phasable.CallState;

public class MyFriendsInteractionsGraphAble extends AbstractInteractionsGraphstreamGraphAble {
	
	@Inject
	public MyFriendsInteractionsGraphAble(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		super(graphstreamInteractionsGraphGenerator);
	}
	
	@Override
	public CallState call() throws Exception {
		graphstreamInteractionsGraphGenerator.clear();
		graphstreamInteractionsGraphGenerator.prepareForDisplay();
		graphstreamInteractionsGraphGenerator.generateMyFriends();
		return callState;
	}
}
