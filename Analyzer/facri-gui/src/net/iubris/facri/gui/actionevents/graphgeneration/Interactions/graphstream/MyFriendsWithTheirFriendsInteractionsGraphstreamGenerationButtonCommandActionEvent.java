package net.iubris.facri.gui.actionevents.graphgeneration.Interactions.graphstream;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers.MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationTaskWrapper;

public class MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent extends AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent {

	private static final long serialVersionUID = 4857914927666678573L;
	private static final String buttonText = "*My Friends With Their Friends* Interactions";
	
	@Inject
	public MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent(MyFriendsWithTheirFriendsInteractionsGraphstreamGenerationTaskWrapper taskWrapper) {
		super(buttonText, taskWrapper );
	}

}
