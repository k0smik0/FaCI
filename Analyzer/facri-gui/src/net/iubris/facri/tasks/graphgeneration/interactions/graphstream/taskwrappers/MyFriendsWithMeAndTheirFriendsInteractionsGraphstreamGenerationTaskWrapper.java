package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables.MyFriendsWithMeAndTheirFriendsInteractionsGraphAble;

public class MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationTaskWrapper extends AbstractInteractionsGraphstreamGenerationTaskWrapper {

	@Inject
	public MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationTaskWrapper(MyFriendsWithMeAndTheirFriendsInteractionsGraphAble graphable) {
		super(graphable);
	}
}
