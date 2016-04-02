package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables.MyFriendsWithMeInteractionsGraphAble;

public class MyFriendsWithMeInteractionsGraphstreamGenerationTaskWrapper extends AbstractInteractionsGraphstreamGenerationTaskWrapper {

	@Inject
	public MyFriendsWithMeInteractionsGraphstreamGenerationTaskWrapper(MyFriendsWithMeInteractionsGraphAble graphable) {
		super(graphable);
	}
}
