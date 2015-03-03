package net.iubris.facri.gui.actionevents.graphgeneration.Interactions.graphstream;

import javax.inject.Inject;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers.MyFriendsWithMeInteractionsGraphstreamGenerationTaskWrapper;

public class MyFriendsWithMeInteractionsGraphstreamGenerationButtonCommandActionEvent extends AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent {

	private static final long serialVersionUID = 7908183508842094014L;
	private static final String buttonText = "*My Friends With Me* Interactions";
	
	@Inject
	public MyFriendsWithMeInteractionsGraphstreamGenerationButtonCommandActionEvent(MyFriendsWithMeInteractionsGraphstreamGenerationTaskWrapper taskWrapper) {
		super(buttonText, taskWrapper);
	}
}
