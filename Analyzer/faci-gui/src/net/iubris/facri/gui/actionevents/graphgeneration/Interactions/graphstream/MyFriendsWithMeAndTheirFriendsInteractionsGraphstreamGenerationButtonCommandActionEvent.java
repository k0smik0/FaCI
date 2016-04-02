package net.iubris.facri.gui.actionevents.graphgeneration.Interactions.graphstream;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers.MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationTaskWrapper;

public class MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent extends AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent {

	private static final long serialVersionUID = 2688544070032564987L;
	private static final String buttonText = "*My Friends With Me And Their Friends* Interactions";
	
	@Inject
	public MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent(MyFriendsWithMeAndTheirFriendsInteractionsGraphstreamGenerationTaskWrapper taskWrapper) {
		super(buttonText, taskWrapper);
	}

}
