package net.iubris.facri.gui.actionevents;

import javax.inject.Inject;

import net.iubris.ishtaran.gui.actionevents.base.ButtonCommandActionEvent;
import net.iubris.ishtaran.gui.actionevents.base.StartbuttonSwingWorker;
import net.iubris.ishtaran.gui.actionevents.base.StopbuttonSwingWorker;
import net.iubris.ishtaran.task.taskwrapper.TaskWrapper;
import net.iubris.ishtaran.utils.printer.Printer;

public class ParseButtonCommandActionEvent extends ButtonCommandActionEvent {

	private static final String buttonText = "Parse";
	
	@Inject
	public ParseButtonCommandActionEvent(TaskWrapper taskWrapper,
			StartbuttonSwingWorker.Factory startbuttonSwingworkerFactory,
			StopbuttonSwingWorker.Factory stopbuttonSwingworkerFactory,
			Printer printer) {
		super(buttonText, taskWrapper, startbuttonSwingworkerFactory, stopbuttonSwingworkerFactory, printer);
	}

	private static final long serialVersionUID = -8599446636567376729L;

}
