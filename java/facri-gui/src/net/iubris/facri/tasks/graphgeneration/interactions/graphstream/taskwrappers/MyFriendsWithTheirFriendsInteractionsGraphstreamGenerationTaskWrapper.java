package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables.MyFriendsWithTheirFriendsInteractionsGraphAble;

public class MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationTaskWrapper extends AbstractInteractionsGraphstreamGenerationTaskWrapper {

	@Inject
	public MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationTaskWrapper(MyFriendsWithTheirFriendsInteractionsGraphAble graphable) {
		super(graphable);
	}
}
