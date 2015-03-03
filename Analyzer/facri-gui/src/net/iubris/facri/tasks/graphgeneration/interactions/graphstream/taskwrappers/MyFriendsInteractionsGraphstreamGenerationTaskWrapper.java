package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables.MyFriendsInteractionsGraphAble;

public class MyFriendsInteractionsGraphstreamGenerationTaskWrapper extends AbstractInteractionsGraphstreamGenerationTaskWrapper {

	@Inject
	public MyFriendsInteractionsGraphstreamGenerationTaskWrapper(MyFriendsInteractionsGraphAble graphable) {
		super(graphable);
	}
}
