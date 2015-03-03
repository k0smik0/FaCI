package net.iubris.facri.gui.actionevents.parse;

import javax.inject.Inject;
import javax.swing.JButton;

import net.iubris.facri.tasks.parse.ParseTaskWrapper;
import net.iubris.ishtaran.gui.actionevents.base.ButtonCommandActionEvent;

public class ParseButtonCommandActionEvent extends ButtonCommandActionEvent {

	private static final String buttonText = "Parse";
	private final JButton button;
	
	@Inject
	public ParseButtonCommandActionEvent(ParseTaskWrapper parseTaskWrapper) {
		super(buttonText, parseTaskWrapper );
		button = super.getButton();
	}
	
	@Override
	protected void actWhenPressStart() {
		super.actWhenPressStart();
		button.setEnabled(false);
		button.setText("waiting for finish...");
	}
	
	@Override
	protected void actPostStart() throws Exception {
		super.actPostStart();
		button.setEnabled(false);
		button.setText("parsed");
	}

	private static final long serialVersionUID = -8599446636567376729L;
}
