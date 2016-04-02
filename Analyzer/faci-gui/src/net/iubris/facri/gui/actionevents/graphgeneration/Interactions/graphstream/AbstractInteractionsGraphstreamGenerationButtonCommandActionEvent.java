package net.iubris.facri.gui.actionevents.graphgeneration.Interactions.graphstream;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers.AbstractInteractionsGraphstreamGenerationTaskWrapper;
import net.iubris.ishtaran.gui.actionevents.base.ButtonCommandActionEvent;

public abstract class AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent extends ButtonCommandActionEvent {

	private static final long serialVersionUID = -3640826620163223397L;

	protected AbstractInteractionsGraphstreamGenerationButtonCommandActionEvent(String buttonText, AbstractInteractionsGraphstreamGenerationTaskWrapper taskWrapper) {
		super(buttonText, taskWrapper );
	}
		
	@Override
	protected void actWhenPressStart() {
		super.actWhenPressStart();
		super.button.setEnabled(false);
		super.button.setText("calculate metrics...");
	}
	
	@Override
	protected void actPostStart() throws Exception {
		super.button.setText(super.buttonText);
		super.button.setEnabled(true);
	}

}
