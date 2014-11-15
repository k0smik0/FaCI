package net.iubris.facri.gui;

import java.util.Set;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import net.iubris.ishtaran.gui.GUIFrame;
import net.iubris.ishtaran.gui._di.annotations.GUIButtons;
import net.iubris.ishtaran.gui._di.annotations.JLabelGlobal;
import net.iubris.ishtaran.gui._di.annotations.JLabelRelative;
import net.iubris.ishtaran.gui._di.annotations.JTextAreaGlobal;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobal;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarRelative;

public class FacriGUIFrame extends GUIFrame {

	@Inject
	public FacriGUIFrame(@GUIButtons Set<JButton> buttons, 
			@JTextAreaGlobal JTextArea jTextArea, 
			@JLabelRelative JLabel jLabelRelative,
			@ProgressBarRelative JProgressBar jProgressBarRelative, 
			@JLabelGlobal JLabel jLabelGlobal, 
			@ProgressBarGlobal JProgressBar jProgressBarGlobal) {
		super(buttons, jTextArea, jLabelRelative, jProgressBarRelative, jLabelGlobal, jProgressBarGlobal);
	}

	private static final long serialVersionUID = 4642194410605183875L;
}
