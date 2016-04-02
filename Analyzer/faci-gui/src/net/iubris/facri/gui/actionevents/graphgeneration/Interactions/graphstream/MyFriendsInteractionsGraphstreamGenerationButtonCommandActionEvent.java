package net.iubris.facri.gui.actionevents.graphgeneration.Interactions.graphstream;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers.MyFriendsInteractionsGraphstreamGenerationTaskWrapper;

public class MyFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent extends AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent {

	private static final long serialVersionUID = 2301232583819951348L;
	private static final String buttonText = "*My Friends* Interactions";
	
	@Inject
	public MyFriendsInteractionsGraphstreamGenerationButtonCommandActionEvent(MyFriendsInteractionsGraphstreamGenerationTaskWrapper taskWrapper) {
		super(buttonText, taskWrapper );
	}
}
