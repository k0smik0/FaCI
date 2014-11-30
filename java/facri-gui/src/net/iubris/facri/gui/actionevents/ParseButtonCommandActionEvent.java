package net.iubris.facri.gui.actionevents;

import javax.inject.Inject;
import javax.swing.JButton;

import net.iubris.facri.tasks.ParseTaskWrapper;
import net.iubris.ishtaran.gui.actionevents.base.AbstractButtonCommandActionEvent;

public class ParseButtonCommandActionEvent extends AbstractButtonCommandActionEvent {

	private static final String buttonText = "Parse";
	
	@Inject
	public ParseButtonCommandActionEvent(ParseTaskWrapper parseTaskWrapper) {
		super(buttonText, parseTaskWrapper );
	}
	
	@Override
	protected void actWhenPressStart() {
		super.actWhenPressStart();
		JButton button = super.getButton();
		button.setEnabled(false);
		button.setText("waiting for finish...");
	}

	private static final long serialVersionUID = -8599446636567376729L;
}
